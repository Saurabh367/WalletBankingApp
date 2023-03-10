package com.FullStack.WalletBanking.EntityUtility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class OtpClass {
    private Integer generatedOTP;
    private int accNumber;

    public OtpClass(Integer generatedOTP, Integer accNumber, Integer userEnteredOTP) {
        this.generatedOTP = generatedOTP;
        this.accNumber = accNumber;
        this.userEnteredOTP = userEnteredOTP;
    }



    private Integer userEnteredOTP;
}
