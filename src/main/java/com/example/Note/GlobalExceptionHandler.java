package com.example.Note;
import com.example.Note.note.SameNoteNameFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler( SameNoteNameFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String,String> SameNoteNameFoundHandler(Exception ex) {
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return map;
    }

}


