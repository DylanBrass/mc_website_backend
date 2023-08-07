package com.mc_website.apigateway.utils.exceptions;

public class ExistingUserNotFoundException extends RuntimeException {
    public ExistingUserNotFoundException(String message) { super(message); }
}
