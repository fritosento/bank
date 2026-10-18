package com.taskmathproject.bank.Handle;

import com.taskmathproject.bank.exeption.ExeptionDTO;
import com.taskmathproject.bank.exeption.IllegalArgException;
import com.taskmathproject.bank.exeption.NotFndException;
import com.taskmathproject.bank.exeption.Null_P_Exception;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleBankModel {

    @ExceptionHandler(IllegalArgException.class)
    public ResponseEntity<ExeptionDTO> IllegalArgs(
            IllegalArgException exeptionDTO
    ) {
        ExeptionDTO ex = ExeptionDTO.builder()
                .message(exeptionDTO.getMessage())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

    @ExceptionHandler(Null_P_Exception.class)
    public ResponseEntity<ExeptionDTO> NullPointer(
            Null_P_Exception exeptionDTO
    ) {
        ExeptionDTO ex = ExeptionDTO.builder()
                .message(exeptionDTO.getMessage())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    }

    @ExceptionHandler(NotFndException.class)
    public ResponseEntity<ExeptionDTO> NotFound(
            NotFndException exeptionDTO
    ) {
        ExeptionDTO ex = ExeptionDTO.builder()
                .message(exeptionDTO.getMessage())
                .errorCode(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
    }

    @ExceptionHandler(PessimisticLockingFailureException.class)
    public ResponseEntity<ExeptionDTO> PessimisticLockFailureException(
            PessimisticLockingFailureException exeptionDTO
    ) {
        ExeptionDTO dto = ExeptionDTO.builder()
                .message(exeptionDTO.getMessage())
                .errorCode(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ExeptionDTO> ObOptimisticLockFailureException(
            ObjectOptimisticLockingFailureException exeptionDTO
    ) {
        ExeptionDTO dto = ExeptionDTO.builder()
                .message(exeptionDTO.getMessage())
                .errorCode(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }
}


