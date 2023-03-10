package com.FullStack.WalletBanking.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Balance {
@Id
    private int uid;
    private int amount;

}

