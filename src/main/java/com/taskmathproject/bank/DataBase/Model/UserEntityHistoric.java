package com.taskmathproject.bank.DataBase.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Historico")
public class UserEntityHistoric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime horario;


    @Column(name = "Origem")
    private String origem;

    @Column(name = "OrigemEmail")
    private String origemEmail;


    @Column(name = "Destino")
    private String destino;

    @Column(name = "DestinoEmail")
    private String destinoEmail;


    @Column(name = "valor")
    private BigDecimal valor;



}
