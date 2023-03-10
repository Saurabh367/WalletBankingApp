package com.FullStack.WalletBanking.Dao.RepoImplementation;

import com.FullStack.WalletBanking.CustomException.NotSufficientBalance;
import com.FullStack.WalletBanking.CustomException.TransactionBadRequest;
import com.FullStack.WalletBanking.Dao.Repository.AccountDetailsRepo;
import com.FullStack.WalletBanking.Dao.Repository.TransactionRepository;

import com.FullStack.WalletBanking.EntityUtility.AccDetailTemp;
import com.FullStack.WalletBanking.Utility.Cashback;
import com.FullStack.WalletBanking.Model.AccountDetails;

import com.FullStack.WalletBanking.Model.Balance;
import com.FullStack.WalletBanking.Model.Domain.Role;
import com.FullStack.WalletBanking.Model.Domain.UserEntity;
import com.FullStack.WalletBanking.Model.RegisterRequest;
import com.FullStack.WalletBanking.Model.Transaction;
import com.FullStack.WalletBanking.Utility.SequenceGeneratorService;

import com.FullStack.WalletBanking.Utility.GenAccountNumber;
import com.FullStack.WalletBanking.Utility.TransactionalValidator;
import com.FullStack.WalletBanking.Utility.WalletResource;



import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class RegistrationDaoImpl  {
    @Autowired
    private AccountDetailsRepo accountDetailsRepo;
    @Autowired
    private AccountDetails acdetails;

    @Autowired
    private  PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SequenceGeneratorService service;

    TransactionalValidator validator = new TransactionalValidator();

    private static final Logger logger = LoggerFactory.getLogger(WalletResource.class);

    double balance;
//    int flag=0;

    public static Map<Integer, AccountDetails> map1 = new HashMap<>();
    public static List<String> transactions= new ArrayList<>();
    public static List<Transaction> allTransactions= new ArrayList<>();



    public AccountDetails createAccount(AccDetailTemp details) throws DuplicateKeyException {

            String email = details.getDetails().getEmail();

           Optional<AccountDetails> byId = accountDetailsRepo.findByDetails_Email(email);

        if(byId.isPresent()){
            System.out.println(byId);
            System.out.println(byId.isPresent());
            throw new DuplicateKeyException("Email address already exists");
        }
        else{
            AccountDetails acc=new AccountDetails();
            int accNumber = GenAccountNumber.generateAccountNumber();
            System.out.println();
            System.out.println("Account Number :"+accNumber);
             acc.setAccNumber(accNumber);
            acc.setBalance(0);
            acc.setDetails(details.getDetails());
            acc.getDetails().setRole(Role.USER);
            acc.getDetails().setPassword(details.getDetails().getPassword());


            map1.put(accNumber, acc);
            System.out.println("Data Added Succesfully");
            System.out.println(map1);
            System.out.println(details);
            return accountDetailsRepo.save(acc);
        }
 }
 public String deleteAccount(int id){
        Optional<AccountDetails> wallet=accountDetailsRepo.findById(id);
        if(accountDetailsRepo.findById(id).isPresent()){
            accountDetailsRepo.delete(wallet.get());
            return "Account Deleted Succesfully";
        }
        else{
            return "No account Found"+id;
        }
 }
    public UserEntity dtoToUser(RegisterRequest user){
        UserEntity newUserEntity=this.modelMapper.map(user,UserEntity.class);
        return newUserEntity;
    }
    public String loggin(String username,String password){
        for(Integer key: map1.keySet()){
            if(acdetails.getDetails().getName().equals(username)&&(acdetails.getDetails().getPassword().equals(password))){
                return "Succesfully Logged In";
            }
        }
        return "Login Failed";

    }

//    public  List<UserEntity> printMap(){
//        return userEntityRepo.findAll();
//    }

    public int deposit(Balance balance) {
         AccountDetails temp = accountDetailsRepo.findById(balance.getUid()).get();


             int v = temp.getBalance() + balance.getAmount();

             temp.setBalance(v);
        System.out.println(temp.getBalance());

             System.out.println(" temp :"+v);
             String deposit = " THE AMOUNT" + v + "IS DEPOSITED";
             transactions.add(deposit);

             accountDetailsRepo.save(temp);

             return  v;

         }
         public AccountDetails showInfo(int accNumber){
             AccountDetails temp = accountDetailsRepo.findById(accNumber).get();
             return temp;
         }


public Transaction sendMoney(@RequestBody Transaction transaction) throws TransactionBadRequest {


    if (!validator.validateRequest(transaction)) {
        throw new TransactionBadRequest("Your Transaction is invalid");
    }
    transaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));

    AccountDetails sender = accountDetailsRepo.findById(transaction.getSid()).get();
    AccountDetails receiver =  accountDetailsRepo.findById(transaction.getRid()).get();

    if (sender == null || receiver == null||sender.getAccNumber()==receiver.getAccNumber()) {
        logger.info("No wallet for sender or receiver");
        throw new TransactionBadRequest("No wallet found for sender or receiver");

    }
    AccountDetails senderWallet = accountDetailsRepo.findById(sender.getAccNumber()).get();
    AccountDetails receiverWallet = accountDetailsRepo.findById(receiver.getAccNumber()).get();

    Transaction senderTransaction = new Transaction();
    senderTransaction.setSid(senderWallet.getAccNumber());
    senderTransaction.setRid(receiverWallet.getAccNumber());
    senderTransaction.setId(UUID.randomUUID().toString());

    int amt = transaction.getAmount();

    if (senderWallet.getBalance() < amt) {
        throw new NotSufficientBalance();
    }
    try {
        int cashb = Cashback.generateCashback();
        System.out.println("Congrulations You got Cashback of " + cashb + " rupees");
        senderWallet.setBalance(senderWallet.getBalance() - amt + cashb);
        senderWallet.setIsRedeemed(cashb + " is redemmed");
        senderTransaction.setAmount(senderWallet.getBalance());
        senderTransaction.setCashback("Congrulations You got Cashback of " + cashb + " rupees");
        senderTransaction.setStatus("SUCCESS");
        senderTransaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        senderTransaction.setMessage("Transferred to Acc. No :[" + receiver.getAccNumber() + "]");
        senderWallet.getTransactions().add(senderTransaction);
        transaction.setMessage("Transferred to Acc. No :[" + receiver.getAccNumber() + "]");
        transaction.setStatus("Success");
        transaction.setCashback("Congrulations You got Cashback of " + cashb + " rupees");
//        System.out.println(senderWallet + " Money of Sender Wallet");
        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setId(UUID.randomUUID().toString());
        receiverWallet.setBalance(receiverWallet.getBalance() + amt);
        receiverTransaction.setAmount(receiverWallet.getBalance());
        receiverTransaction.setCashback("");
        receiverTransaction.setStatus("SUCCESS");
        receiverTransaction.setMessage("Received from Acc. No :[" + sender.getAccNumber() + "]");
        receiverTransaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        receiverWallet.getTransactions().add(receiverTransaction);
        logger.info(String.format("$$ ->  Transaction  Completed --> %s", transaction));
        accountDetailsRepo.save(receiverWallet);
        accountDetailsRepo.save(senderWallet);
        allTransactions.add(senderTransaction);
        allTransactions.add((receiverTransaction));

        return transactionRepository.save(transaction);
    }
    catch (NotSufficientBalance e){
        System.err.println("Caught exception: " + e.getMessage());
        throw e;
    }
    finally {
    }

}
//    public List<Transaction> printTransaction(int userId) {
//        return transactionRepository.findByUserId(userId);
//
//    }
    public double showbalance(int acc_number)
    {

        for(Integer key:map1.keySet()) {

            acdetails=map1.get(key)	;
            if(acdetails.getAccNumber()==acc_number) {
                balance=acdetails.getBalance();
                System.out.println("YOUR BALANCE IS => Rs"+acdetails.getBalance());
            }
            return balance;
        }
        return 0;
    }

    public int bal(int acc){
         AccountDetails temp= accountDetailsRepo.findById(acc).get() ;
         return temp.getBalance();
}

    public List<Transaction> allList(){
        return allTransactions;
    }

    public List<Transaction> userTransaction(int acc){
        AccountDetails temp= accountDetailsRepo.findById(acc).get();
       return temp.getTransactions();

    }
}
