package com.example.Note.exception;

public class NoteNotFoundException extends RuntimeException{
    public NoteNotFoundException() {
        super(String.format("No note found!!!"));
    }
}
