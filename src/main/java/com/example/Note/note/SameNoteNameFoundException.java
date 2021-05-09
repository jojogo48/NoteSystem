package com.example.Note.note;

public class SameNoteNameFoundException extends RuntimeException {
    public SameNoteNameFoundException() {

        super(String.format("Note name already exist !!"));
    }
}