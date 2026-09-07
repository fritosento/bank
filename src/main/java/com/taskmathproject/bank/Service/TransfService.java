package com.taskmathproject.bank.Service;

import com.taskmathproject.bank.Depositar;
import com.taskmathproject.bank.Model.BankModel;
import com.taskmathproject.bank.Repository.BankRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransfService {

    private final BankRepository bankRepository;
    private final HistService histService;

    public TransfService(BankRepository bankRepository, HistService histService) {
        this.bankRepository = bankRepository;
        this.histService = histService;
    }

//    GET

    public List<BankModel>  getAll(){
        return bankRepository.findAll();
    }

    public BankModel getById(Long id){
        return bankRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Id não encontrado")
        );
    }

//    DELETE

    public void delete(Long id){
        bankRepository.deleteById(id);
    }

//    POST

    public BankModel save(BankModel bankModel){
        return bankRepository.save(bankModel);
    }

    public List<BankModel> saveAll(List<BankModel> bankModels){
        return bankRepository.saveAll(bankModels);
    }

    @Transactional
    public ResponseEntity<List<BankModel>> transferirSaldo(Long origem, Long destino, Depositar deposito){
        BankModel saida = bankRepository.findById(origem)
                .orElseThrow(() -> new IllegalArgumentException("Id inexistente"));
        BankModel recebimento = bankRepository.findById(destino)
                .orElseThrow(() -> new IllegalArgumentException("Id inexistente"));

        List<BankModel> transacao =  new ArrayList<>();

        saida.setSaldo(saida.getSaldo().subtract(deposito.getValor()));

        recebimento.setSaldo(recebimento.getSaldo().add(deposito.getValor()));

        transacao.add(saida);
        transacao.add(recebimento);

        bankRepository.saveAll(transacao);

        histService.salvarHistorico(
                saida.getId(),
                recebimento.getId(),
                saida.getNome(),
                recebimento.getNome(),
                deposito);
        
        return ResponseEntity.ok().body(transacao);
    }


//    PUT

    public BankModel updateAllById(Long id, BankModel bankModel){
        BankModel Usuario =  bankRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Id inexistente"));
                Usuario.setCpf(bankModel.getCpf());
                Usuario.setNome(bankModel.getNome());
                Usuario.setSaldo(bankModel.getSaldo());
                return  bankRepository.save(Usuario);
    }

    public BankModel updateNameById(Long id, BankModel bankModel){
        BankModel Usuario =  bankRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Id inexistente"));
                Usuario.setNome(bankModel.getNome());
        return bankRepository.save(Usuario);
    }

    public BankModel depositarById (Long id, BigDecimal valor){

        BankModel Usuario =  bankRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Id inexistente"));

                Usuario.setSaldo(Usuario.getSaldo().add(valor));
        return  bankRepository.save(Usuario);
    }
    public BankModel sacarById (Long id, BigDecimal valor){

        BankModel Usuario =  bankRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Id inexistente"));

                Usuario.setSaldo(Usuario.getSaldo().subtract(valor));
        return  bankRepository.save(Usuario);
    }
}
