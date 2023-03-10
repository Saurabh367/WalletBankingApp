package com.FullStack.WalletBanking.Controller;

import com.FullStack.WalletBanking.Dao.Repository.AccountDetailsRepo;
import com.FullStack.WalletBanking.EntityUtility.AccDetailTemp;
import com.FullStack.WalletBanking.EntityUtility.OtpClass;
import com.FullStack.WalletBanking.EntityUtility.User;
import com.FullStack.WalletBanking.Model.AccountDetails;
import com.FullStack.WalletBanking.Model.Domain.UserEntity;
import com.FullStack.WalletBanking.Model.RegisterRequest;
import com.FullStack.WalletBanking.Service.EmailServiceImpl;
import com.FullStack.WalletBanking.WebConfig.Token.Config.AuthenticationService;
import com.FullStack.WalletBanking.api.AuthenticationRequest;
import com.FullStack.WalletBanking.api.AuthenticationResponse;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reg")
@CrossOrigin(origins = "*")
public class RegistrationController {
    @Autowired
    private AuthenticationService service;
    @Autowired
    private UserEntity user;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private AccountDetailsRepo accountDetailsRepo;
    private static final Logger logger = LoggerFactory.getLogger(AccDetailTemp.class);
    @PostMapping(path = "/register")
    public AuthenticationResponse registerData(@RequestBody RegisterRequest request ){

        return  service.register(request );


    }
    @PostMapping(path="/verify")
    public boolean verifyUsingOtp(@RequestBody  OtpClass otpClassP){
        return service.verifyOTP(otpClassP);
    }


    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest request)   {

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());

        String token=this.service.authenticate(request).getToken();
        AccountDetails byDetailsEmail = accountDetailsRepo.findByDetailsEmail(request.getEmail());
       int accounrNumber= byDetailsEmail.getAccNumber();
        double balance=byDetailsEmail.getBalance();



        AuthenticationResponse response=new AuthenticationResponse();

        response.setToken(token);
        response.setAccNo(accounrNumber);
        response.setBalance(balance);

        response.setUser(this.modelMapper.map((User)userDetails,User.class));

        logger.info("INFO OF USER "+response.getUser());
        showUserInfo(response.getUser());
        return new ResponseEntity<AuthenticationResponse>(response, HttpStatus.OK);
//
//        return ResponseEntity.ok(service.authenticate(request));

    }

    @GetMapping("/acc/info")
    public   String  showUserInfo( @RequestBody User auth){
        return  auth.getName();
    }

    @PostMapping("/sendmail")
    public String sendingMail( String to, String body) throws MessagingException {
        return  emailService.sendEmail(to,body );

    }

}
