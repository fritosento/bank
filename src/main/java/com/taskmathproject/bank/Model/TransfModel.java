package com.taskmathproject.bank.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Historico")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransfModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime horario;

    @Column(name = "OrigemId")
    private Long origemId;

    @Column(name = "DestinoId")
    private Long destinoId;

    @Column(name = "Origem")
    private String origem;

    @Column(name = "Destino")
    private String destino;

    @Column(name = "valor")
    private BigDecimal valor;



}
