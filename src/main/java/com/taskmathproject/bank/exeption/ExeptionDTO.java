package com.taskmathproject.bank.exeption;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExeptionDTO {

    private String message;
    private Integer errorCode;
}
