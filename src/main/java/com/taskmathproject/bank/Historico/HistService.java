package com.taskmathproject.bank.Historico;

import com.taskmathproject.bank.DataBase.Model.UserEntity;
import com.taskmathproject.bank.DataBase.Model.UserEntityHistoric;
import com.taskmathproject.bank.DataBase.Repository.HistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistService {

    private final HistRepository histRepository;

// GET

    public List<UserEntityHistoric>  getAll(){
        return histRepository.findAll();
    }

    public List<UserEntityHistoric> getHistoricoByUser(String email){
        return histRepository.findByOrigemCredencialOrDestinoCredencial(
                email,email, Sort.by(Sort.Direction.DESC,"horario"));
    }


// POST
    public UserEntityHistoric save(UserEntityHistoric transf){
            return histRepository.save(transf);
        }

    public List<UserEntityHistoric> saveAll(List<UserEntityHistoric> histRepositories){
        return histRepository.saveAll(histRepositories);
    }


    public void salvarHistorico(
        UserEntity origem,
        UserEntity destino,
        HistoricoDTO historicoDTO
    ) {

       List<UserEntityHistoric> historico = new ArrayList<>();

       UserEntityHistoric transf = UserEntityHistoric.builder()

            .origem(origem.getNome())
            .origemCredencial(historicoDTO.getCredencialOrigem())

            .destino(destino.getNome())
            .destinoCredencial(historicoDTO.getCredencialDestino())

            .valor(historicoDTO.getValorTransferencia())

            .build();


       historico.add(transf);

       histRepository.saveAll(historico);
    }

}
