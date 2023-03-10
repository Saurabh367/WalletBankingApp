package com.FullStack.WalletBanking.CustomException;


    public class TransactionBadRequest extends RuntimeException {

        public TransactionBadRequest(String message) {
            super(message);
        }

        public TransactionBadRequest(String message, Throwable cause) {
            super(message, cause);
        }
}
