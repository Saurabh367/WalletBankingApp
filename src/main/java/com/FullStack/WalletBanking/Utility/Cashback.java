package com.FullStack.WalletBanking.Utility;

import java.util.Random;

public class Cashback {
    public static int generateCashback(){

        Random random=new Random();
        int bound =100;

        return random.nextInt(bound);

    }
}
