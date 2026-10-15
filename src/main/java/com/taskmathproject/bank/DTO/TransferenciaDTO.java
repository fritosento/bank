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
public class TransferenciaDTO {

        private String origem;
        private String destino;
        private BigDecimal valorTransferencia;

        public TransferenciaDTO(UserEntity userEntity) {
            BeanUtils.copyProperties(userEntity,this);

        }

}
