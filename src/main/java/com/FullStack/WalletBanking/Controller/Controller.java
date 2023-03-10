package com.FullStack.WalletBanking.Controller;
import com.FullStack.WalletBanking.CustomException.NotSufficientBalance;
import com.FullStack.WalletBanking.CustomException.TransactionBadRequest;
import com.FullStack.WalletBanking.Dao.Repository.TransactionRepository;
import com.FullStack.WalletBanking.EntityUtility.AccDetailTemp;
import com.FullStack.WalletBanking.Model.*;
import com.FullStack.WalletBanking.Model.Domain.UserEntity;
import com.FullStack.WalletBanking.Dao.RepoImplementation.RegistrationDaoImpl;
import com.FullStack.WalletBanking.Service.EmailServiceImpl;
import com.FullStack.WalletBanking.WebConfig.Token.Config.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.AllPermission;
import java.util.List;
@RestController
@RequestMapping("/api/v1/all")
@CrossOrigin(origins = "*")
public class Controller {

    @Autowired
    private RegistrationDaoImpl dao;
    @Autowired
    private LogoutService logoutService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private EmailServiceImpl emailService;
    private static final Logger logger = LoggerFactory.getLogger(AccDetailTemp.class);

    @GetMapping
    public ResponseEntity<String> demoClass(){
        return ResponseEntity.ok("Hello from secured endpoint");
    }
    @GetMapping("/acc/info/{accNumber}")
    public   AccountDetails  showUserInfo( @PathVariable int accNumber){
        return  dao.showInfo(accNumber);
    }

    @GetMapping("/acc/{acc}")
    public int showBal( @PathVariable int acc){

        logger.info("Balance availavle:-");return dao.bal(acc);
    }

    @PostMapping("/token")
    public ResponseEntity<String> exampleMethod(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok("Success");
    }
    @PostMapping(path="/create")
    public AccountDetails  createNewAccount(@RequestBody AccDetailTemp adetails) throws DuplicateKeyException {

        return dao.createAccount(adetails);
    }
    @PostMapping("/logout")
    public String logingOut(HttpServletRequest request,
                            HttpServletResponse response,
                            Authentication authentication){
        logoutService.logout(request, response,authentication);
        return "Logout SuccessFully";

    }

//    @PutMapping(path="/update/{id}")
//    public AccountDetails  updateNewAccount(@PathVariable int id,@RequestBody AccDetailTemp adetails) throws Exception {
//            adetails.set_id(id);
//        return dao.createAccount(adetails);
//    }
    @DeleteMapping(path = "/del/{id}")
    public String delAccount(@PathVariable int id){
        return dao.deleteAccount(id);
    }

    @PostMapping(path="/login/")
    public int amountDeposit( @ RequestBody Balance balance) {
        return dao.deposit(balance);

    }
    @GetMapping(path = "/transactions/")
    public List<Transaction> allAvailavleTransaction(){
        logger.info("Transaction is been printed");
        return transactionRepository.findAll();
    }
//
    @PostMapping("/login/send")
    public Transaction transferMoney(@RequestBody Transaction transaction) throws TransactionBadRequest {
        return dao.sendMoney(transaction);

    }
    @GetMapping("/transaction/{accNo}")
    public List<Transaction> printEntities(@PathVariable int accNo)  {
        return dao.userTransaction(accNo);

    }


//    @GetMapping(path="/allusers")
//    public List<Transaction> printAllUsers(int userId) {
//        return dao.printTransaction(userId);
//    }



//    @PostMapping(path="/sendMailAttach")
//    public String sendMailWithAttachment(
//            @RequestBody EmailDetails details) {
//        String status
//                = emailService.sendMailWithAttachment(details);
//
//        return status;
//    }


}
