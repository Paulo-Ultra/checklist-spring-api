package com.learning.springboot.checklistapi.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }
}
