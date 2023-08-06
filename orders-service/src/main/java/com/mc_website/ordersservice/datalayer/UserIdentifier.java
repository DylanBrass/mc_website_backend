package com.mc_website.ordersservice.datalayer;



public class UserIdentifier {
    private String userId;

    public UserIdentifier() {
        this.userId = java.util.UUID.randomUUID().toString();
    }

    public UserIdentifier(String userId) {
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
