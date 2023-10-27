package com.learning.springboot.checklistapi.exception;

public class CategoryServiceException extends RuntimeException{
    public CategoryServiceException(String message){
        super(message);
    }
}
