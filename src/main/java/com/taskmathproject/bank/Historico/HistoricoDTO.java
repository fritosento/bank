package com.taskmathproject.bank.Historico;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class HistoricoDTO {

    String credencialOrigem;
    String credencialDestino;
    BigDecimal valorTransferencia;
}
