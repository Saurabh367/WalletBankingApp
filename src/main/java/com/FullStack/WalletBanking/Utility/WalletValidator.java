package com.FullStack.WalletBanking.Utility;

import com.FullStack.WalletBanking.Model.AccountDetails;

public class WalletValidator {
    public boolean validateWalletRequest(AccountDetails wallet) {
        if (wallet.getAccNumber() == 0) {
            return false;
        }
        return true;
    }
}
