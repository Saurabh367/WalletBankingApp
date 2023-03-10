package com.FullStack.WalletBanking.EntityUtility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentType {
    private Integer paymentTypeId;
    private Character paymentFrom;
    private Character paymentTo;
    private Character paymentType;
}
