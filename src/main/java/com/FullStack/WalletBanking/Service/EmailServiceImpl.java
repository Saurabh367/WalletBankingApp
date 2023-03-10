package com.FullStack.WalletBanking.Service;

import com.FullStack.WalletBanking.Dao.Repository.EmailService;
import com.FullStack.WalletBanking.Model.EmailDetails;


//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    public String sendEmail( String to, String otp ) throws MessagingException {


         MimeMessage message=javaMailSender.createMimeMessage();
         MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setFrom("saurabhmadhure364@gmail.com");
        helper.setTo(to);
        helper.setSubject("Generated Token");
        helper.setText("Your OTP is  :- ", otp);

        if (javaMailSender != null && helper != null) {
            javaMailSender.send(message);
            return "Mail Sent Successfully...";
        } else {
            return "Error while Sending Mail";
        }

    }}


