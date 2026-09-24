package com.taskmathproject.bank.DTO;

import com.taskmathproject.bank.DataBase.Model.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SaqueDTO {

    private String nome;
    private String email;
    private BigDecimal saldo;

    public SaqueDTO(UserEntity userEntity) {
        BeanUtils.copyProperties(userEntity,this);

    }
}
