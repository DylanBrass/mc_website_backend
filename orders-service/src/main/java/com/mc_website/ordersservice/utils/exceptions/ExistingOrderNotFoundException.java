package com.mc_website.ordersservice.utils.exceptions;

public class ExistingOrderNotFoundException extends RuntimeException {
    public ExistingOrderNotFoundException(String message) { super(message); }
}
