package com.FullStack.WalletBanking.api;


import com.FullStack.WalletBanking.EntityUtility.User;
import com.FullStack.WalletBanking.Model.AccountDetails;
import com.FullStack.WalletBanking.Model.Domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;

    private User user;




    private String message;
    private int accNo;
    private double balance;
    private  int otp;
}
