package com.taskmathproject.bank.Handle;

import com.taskmathproject.bank.exeption.ExeptionDTO;
import com.taskmathproject.bank.exeption.IllegalArgExeption;
import com.taskmathproject.bank.exeption.NotFoundExeption;
import com.taskmathproject.bank.exeption.NullPointerExeption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleBankModel{

    @ExceptionHandler(IllegalArgExeption.class)
    public ResponseEntity<ExeptionDTO> IllegalArgs(
            ExeptionDTO exeptionDTO
    ) {
        ExeptionDTO ex = ExeptionDTO.builder()
                .message(exeptionDTO.getMessage())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }
    @ExceptionHandler(NullPointerExeption.class)
    public ResponseEntity<ExeptionDTO> NullPointer(
            ExeptionDTO exeptionDTO
    ) {
        ExeptionDTO ex = ExeptionDTO.builder()
                .message(exeptionDTO.getMessage())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    }
    @ExceptionHandler(NotFoundExeption.class)
    public ResponseEntity<ExeptionDTO> NotFound(
            ExeptionDTO exeptionDTO
    ){
        ExeptionDTO ex = ExeptionDTO.builder()
                .message(exeptionDTO.getMessage())
                .errorCode(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
    }
}


