package com.FullStack.WalletBanking.WebConfig.Token.Config;

import com.FullStack.WalletBanking.Dao.Repository.AccountDetailsRepo;
import com.FullStack.WalletBanking.Dao.Repository.UserRepo;
import com.FullStack.WalletBanking.EntityUtility.AccDetailTemp;
import com.FullStack.WalletBanking.EntityUtility.OtpClass;
import com.FullStack.WalletBanking.EntityUtility.User;
import com.FullStack.WalletBanking.Model.AccountDetails;
import com.FullStack.WalletBanking.Service.EmailServiceImpl;
import com.FullStack.WalletBanking.Utility.GenAccountNumber;
import com.FullStack.WalletBanking.Utility.GenerateOTP;
import com.FullStack.WalletBanking.api.AuthenticationRequest;
import com.FullStack.WalletBanking.api.AuthenticationResponse;


import com.FullStack.WalletBanking.Model.Domain.Role;


import com.FullStack.WalletBanking.Model.RegisterRequest;
import com.FullStack.WalletBanking.Utility.SequenceGeneratorService;
import com.FullStack.WalletBanking.WebConfig.Token.Token;
import com.FullStack.WalletBanking.WebConfig.Token.TokenRepository;
import com.FullStack.WalletBanking.WebConfig.Token.TokenType;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Component
public class AuthenticationService {
    @Autowired
    private   PasswordEncoder passwordEncoder;
    @Autowired
    private   JwtService jwtService;
    @Autowired
    private UserRepo repository;
    @Autowired
    private  TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private SequenceGeneratorService service;
    @Autowired
    private User us;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AccountDetailsRepo accountDetailsRepo;
@Autowired
private AccountDetails accountDetails;
    private String jwtSecret;
    @Autowired
    private EmailServiceImpl emailService;
    private static final Logger logger = LoggerFactory.getLogger(AccDetailTemp.class);


    public AuthenticationResponse register(RegisterRequest request) {
        try {

            String email = request.getEmail();

            Optional<User> byEmail = userRepo.findByEmail(email);
            if (!byEmail.isPresent()) {

                us.setUserId(service.generateSequenceNumber(RegisterRequest.SEQUENCE_NAME));
                us.setEmail(request.getEmail());
                us.setRole(Role.USER);
                us.setName(request.getName());
                us.setPassword(passwordEncoder.encode(request.getPassword()));
                var savedUser = repository.save(us);
                int generatedAcc = GenAccountNumber.generateAccountNumber();
                accountDetails.setDetails(us);
                accountDetails.setBalance(0);
                accountDetails.setAccNumber(generatedAcc);

                accountDetailsRepo.save(accountDetails);
                var acountDetailsRepo= accountDetailsRepo.save(accountDetails);
//                var userDetails = new org.springframework.security.core.userdetails.User(request.getEmail(), request.getPassword(), new ArrayList<>());

                var jwtToken = jwtService.generateToken((UserDetails) us );
                var authenticationRequest= new AuthenticationRequest();
                GenerateOTP generateOTP=new GenerateOTP();
//                int requestOtp=
                Integer otp = generateOTP.otp_generate();
                String message = "This is Your OTP :  " + otp;
                emailService.sendEmail(request.getEmail(), message);
                // send activation email

                authenticationRequest.setEmail(request.getEmail());
                authenticationRequest.setPassword(request.getPassword());
                String mess="account Number generated is "+generatedAcc;
                var authResponse=this.authenticate(authenticationRequest);
//                jwtService.generateActivationUrl(request.getEmail(),jwtToken)
//                emailService.sendEmail(request.getEmail(),message);
                System.out.println(authResponse);
//                this.authenticate()
                saveUserToken(savedUser, jwtToken);

                System.out.println("Activation email has been sent to " + request.getEmail());

                return AuthenticationResponse.builder()
                        .token(jwtToken).user(us).message(mess).accNo(generatedAcc).otp(otp)
                        .build();
            }
            else{
                System.out.println("Email Id is already present please Use another");
                return  null;
            }
        }
        catch (NoSuchElementException e) {
            return new AuthenticationResponse();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean verifyOTP( OtpClass otpClassP){
        Integer generatedOTP = otpClassP.getGeneratedOTP();
        Integer userEnteredOTP = otpClassP.getUserEnteredOTP();
        Integer account=otpClassP.getAccNumber();

        System.out.println(generatedOTP+" "+userEnteredOTP);
        System.out.println( otpClassP);
        if (otpClassP.getGeneratedOTP() == null) {
            Optional<AccountDetails> wallet=accountDetailsRepo.findById(account);

            accountDetailsRepo.delete(wallet.get());
            logger.info("Account no"+wallet+"Succesfully Deleted");
            System.out.println("Generated OTP is null");
            return false;
        }
        if (otpClassP.getUserEnteredOTP() == null) {
            Optional<AccountDetails> wallet=accountDetailsRepo.findById(account);

                accountDetailsRepo.delete(wallet.get());
                logger.info("Account no"+wallet+"Succesfully Deleted");

            System.out.println("User-entered OTP is null");

            return false;
        }
        System.out.println("Generated OTP: " + otpClassP.getGeneratedOTP());
        System.out.println("User-entered OTP: " + otpClassP.getUserEnteredOTP());
        System.out.println(generatedOTP.equals(userEnteredOTP));
        if(generatedOTP.equals(userEnteredOTP)){
            return true;
        }
      else{

            System.out.println("Generated OTP is null");
          return  false;
        }
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );


        var user=repository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken=jwtService.generateToken((UserDetails) user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }
    private void saveUserToken(User user, String jwtToken) {
        Token token=new Token();
        token.setId(service.generateSequenceNumberToken(Token.SEQUENCE_TOKEN));
        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    public ResponseEntity<String> acceptingToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");

        if (validateToken(token)) {
            return ResponseEntity.ok("Success!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
