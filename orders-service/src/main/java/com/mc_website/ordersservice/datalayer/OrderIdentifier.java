package com.mc_website.ordersservice.datalayer;

import java.util.UUID;

public class OrderIdentifier {
    private String OrderIdentifier;

    public OrderIdentifier() {
        this.OrderIdentifier =  java.util.UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return OrderIdentifier;
    }

    public void setOrderId(String orderIdentifier) {
        OrderIdentifier = orderIdentifier;
    }
}
