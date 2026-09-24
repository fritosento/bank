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
public class UpdateUserNameDTO {

    private String nome;
    private String emailVerify;
    private String cpfVerify;

    public UpdateUserNameDTO(UserEntity userEntity) {
        BeanUtils.copyProperties(userEntity,this);

    }

}
