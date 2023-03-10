package com.FullStack.WalletBanking.Controller;
public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(int id) {

        super("Wallet id not found : " + id);
    }
}
