package com.example.Note.exceptionHandler;
import com.example.Note.exception.NoAccessToFileException;
import com.example.Note.exception.NotLoginException;
import com.example.Note.exception.NoteNotFoundException;
import com.example.Note.exception.SameNoteNameFoundException;
import io.jsonwebtoken.JwtException;
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
    @ResponseBody
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String,String> NotLoginExceptionHandler(Exception ex) {
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return map;
    }


    @ResponseBody
    @ExceptionHandler(NoAccessToFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String,String> NoAccessToFileExceptionHandler(Exception ex) {
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return map;
    }

    @ResponseBody
    @ExceptionHandler(NoteNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String,String> NoteNotFoundExceptionHandler(Exception ex) {
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return map;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String,String> JwtExceptionHandler(Exception ex) {
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return map;
    }


}


