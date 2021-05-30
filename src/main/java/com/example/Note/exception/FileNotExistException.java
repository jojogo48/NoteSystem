package com.example.Note.exception;

public class FileNotExistException extends  RuntimeException{
    public FileNotExistException() {
        super(String.format("file not exist!"));
    }
}


