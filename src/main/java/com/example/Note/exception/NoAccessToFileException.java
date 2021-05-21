package com.example.Note.exception;

public class NoAccessToFileException extends  RuntimeException{
    public NoAccessToFileException() {
        super(String.format("No access to this file"));
    }
}
