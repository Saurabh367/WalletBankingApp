package com.FullStack.WalletBanking.CustomException;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException( String s) {
        super("User id not found : "  );
    }
}
