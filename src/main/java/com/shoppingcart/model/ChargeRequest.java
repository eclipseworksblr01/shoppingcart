package com.shoppingcart.model;

import lombok.Data;

@Data
public class ChargeRequest {

    public enum Currency {
        EUR, USD;
    }

    private String description;
    private double amount; // cents
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}
