package com.mc_website.ordersservice.datalayer;

public class OrderIdentifier {
    private String orderId;

    public OrderIdentifier() {
        this.orderId =  java.util.UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderIdentifier) {
        orderId = orderIdentifier;
    }
}
