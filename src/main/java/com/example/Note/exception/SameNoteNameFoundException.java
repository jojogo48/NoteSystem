package com.example.Note.exception;

public class SameNoteNameFoundException extends RuntimeException {
    public SameNoteNameFoundException() {

        super(String.format("Note name already exist !!"));
    }
}