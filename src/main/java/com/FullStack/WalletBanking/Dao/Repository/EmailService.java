package com.FullStack.WalletBanking.Dao.Repository;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

public interface EmailService {
    String  sendEmail( String to, String body) throws MessagingException;
}
