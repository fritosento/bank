package com.taskmathproject.bank.DTO;

import com.taskmathproject.bank.DataBase.Model.UserEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositoDTO {

    String email;
    BigDecimal valor;

    public DepositoDTO(UserEntity userEntity) {
        BeanUtils.copyProperties(userEntity,this);

    }

}