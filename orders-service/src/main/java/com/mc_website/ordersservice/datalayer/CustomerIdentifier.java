package com.mc_website.ordersservice.datalayer;



public class CustomerIdentifier {
    private String customerId;

    public CustomerIdentifier() {
        this.customerId = java.util.UUID.randomUUID().toString();
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
