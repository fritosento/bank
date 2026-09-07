package com.taskmathproject.bank.Handle;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleBankModel{

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgs(
            IllegalArgumentException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> NullPointer(
            NullPointerException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

}


