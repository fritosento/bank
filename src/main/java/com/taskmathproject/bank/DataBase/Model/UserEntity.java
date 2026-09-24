package com.taskmathproject.bank.DataBase.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User_Table",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "cpf"),
        @UniqueConstraint(columnNames = "email")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CPF
    @NotBlank
    @Column(name = "cpf")
    private String cpf;

    @NotBlank
    @Column(name = "nome")
    private String nome;

    @Email
    @NotBlank
    @Column(name = "email")
    private String email;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @Column(name = "limiteDiario")
    private BigDecimal limiteDiario;

}
