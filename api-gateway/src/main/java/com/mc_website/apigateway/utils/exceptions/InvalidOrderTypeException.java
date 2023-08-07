package com.mc_website.apigateway.utils.exceptions;

public class InvalidOrderTypeException extends RuntimeException{
    public InvalidOrderTypeException(String message) { super(message); }
}
