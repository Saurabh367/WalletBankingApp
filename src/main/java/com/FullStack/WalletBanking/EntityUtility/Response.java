package com.FullStack.WalletBanking.EntityUtility;

import com.FullStack.WalletBanking.Model.AccountDetails;

import lombok.Data;

import java.util.List;
@Data
public class Response {
    private List<AccountDetails> list;
}
