package com.taskmathproject.bank.Service;

import com.taskmathproject.bank.DTO.*;
import com.taskmathproject.bank.DataBase.Model.UserEntity;
import com.taskmathproject.bank.DataBase.Repository.BankRepository;
import com.taskmathproject.bank.exeption.IllegalArgExeption;
import com.taskmathproject.bank.exeption.NotFoundExeption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TransfService {

    private final BankRepository bankRepository;

    private final HistService histService;


//    GET

    public List<ResponseUserDTO> getAll(int pagina) {
        Page<UserEntity> page = bankRepository.findAll(
                PageRequest.of(pagina, 2)
                );

        return page.getContent()
                .stream()
                .map(ResponseUserDTO::new)
                .toList();
    }

    public ResponseUserDTO getById(Long id) {
        UserEntity userDTO = bankRepository.findById(id).orElseThrow(
                () -> new NotFoundExeption("Id não encontrado")
        );

        return new  ResponseUserDTO(userDTO);
    }

    public ResponseUserDTO getByName(String nome) {
        UserEntity user = bankRepository.findByNome(nome)
                .orElseThrow( () -> new NotFoundExeption("Nome Invalido/Não encontrado"));


        return new ResponseUserDTO(user);
    }

    public ResponseUserDTO getByCpf(String cpf) {

        UserEntity user = bankRepository.findByCpf(cpf)
                .orElseThrow(() -> new NotFoundExeption("CPF invalido"));


        return new ResponseUserDTO(user);

    }

//    POST

    public void save(CreateUserDTO userDTO) {
        UserEntity userEntity =  UserEntity.builder()
                .nome(userDTO.getNome())
                .cpf(userDTO.getCpf())
                .email(userDTO.getEmail())
                .saldo(userDTO.getSaldo())
                .limiteDiario(userDTO.getLimiteDiario())
                .build();

        bankRepository.save(userEntity);

    }

    public void saveAll(List<CreateUserDTO> userDTO) {
        List<UserEntity> userEntity = userDTO.stream()
                .map(user -> UserEntity.builder()
                .nome(user.getNome())
                .cpf(user.getCpf())
                .email(user.getEmail())
                .saldo(user.getSaldo())
                .limiteDiario(user.getLimiteDiario())
                .build()
            )
            .toList();

        bankRepository.saveAll(userEntity);
    }

    @Transactional
    public void transferirSaldo(TransferenciaDTO dto) {

        UserEntity saida =
                bankRepository.findByEmail(dto.getOrigemEmail())
                .orElseThrow(() -> new NotFoundExeption("Email não encontrado")
        );

        UserEntity recebimento =
                bankRepository.findByEmail(dto.getDestinoEmail())
                .orElseThrow(() -> new NotFoundExeption("Email não encontrado")
        );

        if (dto.getValorTransferencia().compareTo(saida.getLimiteDiario()) > 0) {
            throw new IllegalArgExeption("Valor de transferência excedeu o limite diário");
        }
        if (dto.getValorTransferencia().compareTo(saida.getSaldo()) > 0 ){
            throw new IllegalArgExeption("Saldo insuficiente");
        }

        saida.setLimiteDiario(saida.getLimiteDiario().subtract(dto.getValorTransferencia()));

        List<UserEntity> transacao = new ArrayList<>();

        saida.setSaldo(saida.getSaldo().subtract(dto.getValorTransferencia()));

        recebimento.setSaldo(recebimento.getSaldo().add(dto.getValorTransferencia()));

        transacao.add(saida);
        transacao.add(recebimento);

        bankRepository.saveAll(transacao);

        histService.salvarHistorico( saida, recebimento, dto.getValorTransferencia() );
    }


//    PUT

    @Transactional
    public void update(UpdateUserNameDTO dto) {
        UserEntity user ;

        if(dto.getEmailVerify() != null && !dto.getEmailVerify().isEmpty()) {
            user = bankRepository.findByEmail(dto.getEmailVerify()).orElseThrow(
                    () -> new IllegalArgExeption("Email inexistente"));

        }else if (dto.getCpfVerify() != null && !dto.getCpfVerify().isEmpty()) {
            user = bankRepository.findByCpf(dto.getCpfVerify())
                    .orElseThrow( () -> new IllegalArgExeption("CPF invalido"));
        }
        else{
            throw new IllegalArgExeption("Identificador invalido");
        }
        user.setNome(dto.getNome());

        bankRepository.save(user);
    }


    public void depositarByEmail(String email, BigDecimal valor) {

        UserEntity Usuario = bankRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgExeption("Email inexistente")
        );

        Usuario.setSaldo(Usuario.getSaldo().add(valor));
         bankRepository.save(Usuario);
    }

    public SaqueDTO sacarByEmail(String email, BigDecimal valor) {

        UserEntity Usuario = bankRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgExeption("Id inexistente"));

        Usuario.setSaldo(Usuario.getSaldo().subtract(valor));
        bankRepository.save(Usuario);

        return new SaqueDTO(Usuario) ;

    }

//    DELETE

    public void delete(Long id) {
        bankRepository.deleteById(id);
    }
    public void deleteAll() {
        bankRepository.deleteAll();
    }
}
