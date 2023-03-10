package com.FullStack.WalletBanking.Service;

public class DataValidation {
    public boolean validateEmailId (String email_id) {

        if(email_id.endsWith(".com"))
        {
            return true;
        }

        else {
            System.err.println("invalid email id");
            return false;
        }
    }
    public boolean validatePhNumber(String phone_number) {

        if(phone_number.length()==10)
        {
            return true;
        }
        else {
            System.err.println("phone number invalid");
            return false;
        }}

    public boolean validateUserName(String customer_name) {
        if(!(customer_name.isEmpty()))
        {
            return true;
        }

        else {
            System.out.println("Customer name is empty");
            return false;
        }
    }

}
