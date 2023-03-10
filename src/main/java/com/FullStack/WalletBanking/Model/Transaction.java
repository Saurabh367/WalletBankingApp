package com.FullStack.WalletBanking.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.mail.Message;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection ="Transactions")
public class Transaction {


    @Id
    private String id;
    private int amount;
//    private String  password;

    public Transaction(int amount, int sid, int rid, Date date, String message, String status, String cashback) {
        this.amount = amount;
        this.sid = sid;
        this.rid = rid;
        this.date = date;
        Message = message;
        this.status = status;
        this.cashback = cashback;
//        this.password=password;
    }

    private int sid;//sender's id
    private int rid;//receiver id
    private Date date;
    private String Message;
    private String status;
    private String cashback;

}
