package com.taskmathproject.bank.Controller;

import com.taskmathproject.bank.DTO.*;
import com.taskmathproject.bank.Depositar;
import com.taskmathproject.bank.DataBase.Model.UserEntityHistoric;
import com.taskmathproject.bank.Service.HistService;
import com.taskmathproject.bank.Service.TransfService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/Transferencias")
@RequiredArgsConstructor
public class TransfController {

    private final TransfService transfService;

    private final HistService histService;


    @GetMapping("/AllClients")

    public ResponseEntity<List<ResponseUserDTO>> getClientes(
            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0)  int page
    ){
        return ResponseEntity.ok(transfService.getAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getClientesById(@PathVariable Long id){
        return ResponseEntity.ok(transfService.getById(id));
    }

    @GetMapping("/{nome}")
    public ResponseEntity<ResponseUserDTO> getClienteByName(@PathVariable String nome){
        return  ResponseEntity.ok(transfService.getByName(nome));
    }
    @GetMapping("/{cpf}")
    public ResponseEntity<ResponseUserDTO> getClienteByCpf(@PathVariable String cpf){
        return  ResponseEntity.ok(transfService.getByCpf(cpf));
    }

    @GetMapping("/Historico")
    public ResponseEntity<List<UserEntityHistoric>> getHistorico(){
        return ResponseEntity.ok(histService.getAll());
    }

    @GetMapping("/Historico/{id}")
    public  ResponseEntity<List<UserEntityHistoric>> getHistoricoByEmail(@PathVariable String email){
        return ResponseEntity.ok(histService.getHistoricoByUser(email));
    }

    @PostMapping
    public  ResponseEntity<Void> createCliente(
            @RequestBody @Valid CreateUserDTO userDTO)
    {
        transfService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/All")
    public ResponseEntity<List<Void>> createAllClientes (
            @RequestBody List<@Valid CreateUserDTO> userDTO
    ){
        transfService.saveAll(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/transferir")
    public ResponseEntity<List<Void>> transferirSaldo(
            @RequestBody TransferenciaDTO dto
    ){

        if (dto.getOrigemEmail().isEmpty() || dto.getDestinoEmail().isEmpty()){
            throw new IllegalArgumentException("Email invalido");

        }

        if (dto.getOrigemEmail().equals(dto.getDestinoEmail())){
            throw new IllegalArgumentException("Os emails não podem ser iguais");
        }

        if(dto.getValorTransferencia().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor inválido");
        }
        transfService.transferirSaldo(dto);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/Update")
    public ResponseEntity<Void> updateCliente(
            @RequestBody @Valid UpdateUserNameDTO dto
    ){

        transfService.update(dto);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/Deposito/{email}")
    public ResponseEntity<Void> depositarSaldo(
            @PathVariable String email,
            @RequestBody @Valid Depositar deposito
    ){

        if(deposito.getValor().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor inválido");
        }

         transfService.depositarByEmail(email, deposito.getValor());
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/Saque/{email}")
    public ResponseEntity<SaqueDTO> saqueSaldo(
            @PathVariable String email,
            @RequestBody @Valid Depositar deposito
    ){

        if(deposito.getValor().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor inválido");
        }

        return ResponseEntity.ok(transfService.sacarByEmail(email, deposito.getValor()));

    }

    @DeleteMapping("/encerrar/{id}")
    public void delete(@PathVariable     Long id) {
        if (transfService.getById(id).getSaldo().compareTo(BigDecimal.ZERO) > 0 ) {return;}

        transfService.delete(id);
    }
    @DeleteMapping("encerrarAll")
    public void deleteAll() {

        transfService.deleteAll();
    }

}
