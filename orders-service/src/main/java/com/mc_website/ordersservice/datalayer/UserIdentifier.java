package com.mc_website.ordersservice.datalayer;



public class UserIdentifier {
    private String customerId;

    public UserIdentifier() {
        this.customerId = java.util.UUID.randomUUID().toString();
    }

    public UserIdentifier(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
