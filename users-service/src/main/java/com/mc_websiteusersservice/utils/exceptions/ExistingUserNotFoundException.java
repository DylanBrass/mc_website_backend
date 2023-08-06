package com.mc_websiteusersservice.utils.exceptions;

public class ExistingUserNotFoundException extends RuntimeException {
    public ExistingUserNotFoundException(String message) { super(message); }
}
