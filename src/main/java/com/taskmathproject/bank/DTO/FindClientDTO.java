package com.taskmathproject.bank.DTO;

import com.taskmathproject.bank.DataBase.Model.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FindClientDTO {

    @NotBlank
    String nome;
    @Email
    String email;
    @CPF
    String cpf;
    public FindClientDTO(UserEntity userEntity) {
        BeanUtils.copyProperties(userEntity,this);

    }
}
