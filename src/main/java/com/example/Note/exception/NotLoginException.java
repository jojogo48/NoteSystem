package com.example.Note.exception;

public class NotLoginException extends RuntimeException{
    public NotLoginException() {
        super(String.format("Please Login!"));
    }
}
