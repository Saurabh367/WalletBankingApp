package com.FullStack.WalletBanking.CustomException;

public class NotSufficientBalance extends RuntimeException {

    public NotSufficientBalance() {
        super("TransactionBadRequest");

    }
}
