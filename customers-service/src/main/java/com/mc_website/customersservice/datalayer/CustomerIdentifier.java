package com.mc_website.customersservice.datalayer;

import jakarta.persistence.Embeddable;

@Embeddable
public class CustomerIdentifier {
    private String customerId;

    public CustomerIdentifier() {
        this.customerId = java.util.UUID.randomUUID().toString();
    }

    public CustomerIdentifier(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
