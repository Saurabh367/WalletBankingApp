package com.FullStack.WalletBanking.EntityUtility;

import com.FullStack.WalletBanking.Model.Domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccDetailTemp {

    private int _id;

    private int accNumber;
    private double balance;
    private User details;







}
