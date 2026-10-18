package com.taskmathproject.bank.Controller;

import com.taskmathproject.bank.DTO.*;
import com.taskmathproject.bank.Depositar;
import com.taskmathproject.bank.DataBase.Model.UserEntityHistoric;
import com.taskmathproject.bank.Historico.HistService;
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
    @GetMapping("/Nome")
    public ResponseEntity<ResponseUserDTO> getClientesByName(
            @RequestParam(value = "nome", defaultValue = "")String nome
    ){
        return ResponseEntity.ok(transfService.getByName(nome));
    }

    @GetMapping("/clientes")
    public ResponseEntity<ResponseUserDTO> getClientesByDTO(
            @RequestParam(required = false) @Valid String credencial
    ){
        return ResponseEntity.ok(transfService.buscarCliente(credencial));
    }



    @GetMapping("/Historico")
    public ResponseEntity<List<UserEntityHistoric>> getHistorico(){
        return ResponseEntity.ok(histService.getAll());
    }

    @GetMapping("/Historico/{email}")
    public  ResponseEntity<List<UserEntityHistoric>> getHistoricoByEmail(@RequestParam String email){
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
            @RequestParam String origem,
            @RequestParam String destino,
            @RequestParam BigDecimal valor
    ){
        TransferenciaDTO dto = TransferenciaDTO
                .builder()
                .origem(origem)
                .destino(destino)
                .valorTransferencia(valor)
                .build();

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

    @PutMapping("/Deposito")
    public ResponseEntity<Void> depositarSaldo(
            @RequestParam String email,
            @RequestParam BigDecimal valor
//            @PathVariable String email,
//            @RequestBody Depositar deposito
    ){
        DepositoDTO dto = DepositoDTO
                .builder()
                .email(email)
                .valor(valor)
                .build();
        if(dto.getValor().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor inválido");
        }

         transfService.depositarByEmail(dto.getEmail(), dto.getValor());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/Saque/{email}")
    public ResponseEntity<SaqueDTO> saqueSaldo(
            @PathVariable String email,
            @RequestBody Depositar deposito
    ){

        if(deposito.getValor().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Valor inválido");
        }

        return ResponseEntity.ok(transfService.sacarByEmail(email, deposito.getValor()));

    }

    @DeleteMapping("/encerrar/{id}")
    public void delete(@PathVariable  Long id) {
        if (transfService.getById(id).getSaldo().compareTo(BigDecimal.ZERO) > 0 ) {return;}

        transfService.delete(id);
    }
    @DeleteMapping("encerrarAll")
    public void deleteAll() {

        transfService.deleteAll();
    }

}
