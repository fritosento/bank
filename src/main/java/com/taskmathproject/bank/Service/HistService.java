package com.taskmathproject.bank.Service;

import com.taskmathproject.bank.Depositar;
import com.taskmathproject.bank.Model.TransfModel;
import com.taskmathproject.bank.Repository.HistRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistService {

    private final HistRepository histRepository;



    public HistService(HistRepository histRepository) {

        this.histRepository = histRepository;
    }


// GET

    public List<TransfModel>  getAll(){
        return histRepository.findAll();
    }

    public ResponseEntity<List<TransfModel>> getHistoricoByUser(Long id){
        List<TransfModel> historico = histRepository.findByOrigemIdOrDestinoId(id,id);
        return ResponseEntity.ok(historico);
    }


// POST
    public TransfModel save(TransfModel transf){
            return histRepository.save(transf);
        }

    public List<TransfModel> saveAll(List<TransfModel> histRepositories){
        return histRepository.saveAll(histRepositories);
    }


    public void salvarHistorico(
            Long origemId,
            Long destinoId,
            String origem,
            String destino,
            Depositar deposito
    ) {

    TransfModel transf = TransfModel.builder().
                origemId(origemId).
                destinoId(destinoId).
                origem(origem).
                destino(destino).
                valor(deposito.getValor())
                .build();

        List<TransfModel> historico = new ArrayList<>();

        historico.add(transf);

        histRepository.saveAll(historico);
    }

}
