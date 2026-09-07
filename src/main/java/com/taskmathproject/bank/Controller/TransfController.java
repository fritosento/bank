package com.taskmathproject.bank.Controller;

import com.taskmathproject.bank.Depositar;
import com.taskmathproject.bank.Model.TransfModel;
import com.taskmathproject.bank.Service.HistService;
import com.taskmathproject.bank.Service.TransfService;
import com.taskmathproject.bank.Model.BankModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/Transferencias")
public class TransfController {

    private final TransfService transfService;
    private final HistService histService;

    public TransfController(TransfService transfService, HistService histService) {
        this.transfService = transfService;
        this.histService = histService;
    }

    @GetMapping
    public List<BankModel> getClientes(){
        return transfService.getAll();
    }

    @GetMapping("/{id}")
    public BankModel getClientesById(@PathVariable Long id){
        return transfService.getById(id);
    }

    @GetMapping("/Historico")
    public List<TransfModel> getHistorico(){
        return histService.getAll();
    }

    @GetMapping("/Historico/{id}")
    public ResponseEntity<List<TransfModel>> getHistoricoById(@PathVariable Long id){
        return histService.getHistoricoByUser(id);
    }

    @PostMapping
    public BankModel createCliente(
            @RequestBody BankModel bankModel)
    {

        return transfService.save(bankModel);
    }
    @PostMapping("/All")
    public ResponseEntity<List<BankModel>> createAllClientes (
            @RequestBody List<BankModel> bankModels
    ){
        return ResponseEntity.ok(transfService.saveAll(bankModels));
    }

    @PostMapping("/{origem}/transferir/{destino}")
    public ResponseEntity<List<BankModel>> transferirSaldo(
            @PathVariable Long origem,
            @PathVariable Long destino,
            @RequestBody Depositar deposito
    ){
        if(deposito.getValor().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor inválido");
        }
        if(origem < 1 || destino < 1){
            throw new NullPointerException("Id recebido invalido");
        }
        if (origem.equals(destino)){
            throw new IllegalArgumentException("Id´s não podem ser iguais");
        }
        if (transfService.getById(origem).getSaldo().compareTo(deposito.getValor()) < 0 ){
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        return transfService.transferirSaldo(origem, destino, deposito);
    }


    @PutMapping("/Update/All/{id}")
    public BankModel updateClienteById(
            @PathVariable Long id,
            @RequestBody BankModel bankModel
    ){
        return transfService.updateAllById(id, bankModel);
    }

    @PutMapping("/Update/Name/{id}")
    public BankModel updateNameById(
            @PathVariable Long id,
            @RequestBody BankModel bankModel
    ){
        return transfService.updateNameById(id, bankModel);
    }

    @PutMapping("/Deposito/{id}")
    public BankModel depositarSaldo(
            @PathVariable Long id,
            @RequestBody Depositar deposito
    ){

        if(deposito.getValor().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor inválido");
        }
        return transfService.depositarById(id, deposito.getValor());

    }
    @PutMapping("/Saque/{id}")
    public BankModel saqueSaldo(
            @PathVariable Long id,
            @RequestBody Depositar deposito
    ){

        if(deposito.getValor().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor inválido");
        }

        return transfService.sacarById(id, deposito.getValor());

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transfService.delete(id);
    }
}
