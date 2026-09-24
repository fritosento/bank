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
public class TransferenciaDTO {

        private String origemEmail;
        private String destinoEmail;
        private BigDecimal valorTransferencia;

        public TransferenciaDTO(UserEntity userEntity) {
            BeanUtils.copyProperties(userEntity,this);

        }

}
