package com.taskmathproject.bank.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "User_Table")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @Column(name = "saldo")
    private BigDecimal saldo;

}
