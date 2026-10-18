package com.taskmathproject.bank.exeption;

public class IllegalArgException extends RuntimeException {
    public IllegalArgException(String message) {
        super(message);
    }
}
