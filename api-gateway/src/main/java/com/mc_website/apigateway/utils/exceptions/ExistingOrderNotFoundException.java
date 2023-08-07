package com.mc_website.apigateway.utils.exceptions;

public class ExistingOrderNotFoundException extends RuntimeException {
    public ExistingOrderNotFoundException(String message) { super(message); }
}
