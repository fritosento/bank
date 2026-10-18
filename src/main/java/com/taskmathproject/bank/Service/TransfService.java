package com.taskmathproject.bank.Service;

import com.taskmathproject.bank.DTO.*;
import com.taskmathproject.bank.DataBase.Model.UserEntity;
import com.taskmathproject.bank.DataBase.Repository.BankRepository;
import com.taskmathproject.bank.Historico.HistService;
import com.taskmathproject.bank.Historico.HistoricoDTO;
import com.taskmathproject.bank.exeption.IllegalArgException;
import com.taskmathproject.bank.exeption.NotFndException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
                () -> new NotFndException("Id não encontrado")
        );


        return new ResponseUserDTO(userDTO);
    }

    public ResponseUserDTO getByName(String nome) {
        UserEntity user = bankRepository.findByNomeIgnoreCaseContaining(nome)
                .orElseThrow(() -> new NotFndException("Nome Invalido/Não encontrado"));


        return new ResponseUserDTO(user);
    }

    public ResponseUserDTO getByCpf(String cpf) {

        UserEntity user = bankRepository.findByCpf(cpf)
                .orElseThrow(() -> new NotFndException("CPF invalido"));


        return new ResponseUserDTO(user);

    }

    public ResponseUserDTO getByEmail(String email) {
        UserEntity user = bankRepository.findByCpf(email).orElseThrow(
                () -> new NotFndException("Email invalido")
        );
        return new ResponseUserDTO(user);
    }
//    POST

    public void save(CreateUserDTO userDTO) {
        UserEntity userEntity = UserEntity.builder()
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
        String origem = dto.getOrigem() == null ? "" : dto.getOrigem().trim();
        String destino = dto.getDestino() == null ? "" : dto.getDestino().trim();
        BigDecimal saldo = dto.getValorTransferencia();

        UserEntity u1 = new UserEntity();
        UserEntity u2 = new UserEntity();

        String cpfV1 = origem.replaceAll("\\D", "");
        String cpfV2 = destino.replaceAll("\\D", "");

        HistoricoDTO historicoDTO = new HistoricoDTO();

        if (saldo == null || saldo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgException("Valor inválido");
        }

        if (origem.isBlank() || destino.isBlank()) {
            throw new IllegalArgException("Erro no processo de transferência! Verifique as credenciais.");
        }
        if (origem.equals(destino)) {
            throw new IllegalArgException("Credenciais precisam ser diferentes.");
        }

        String primeiro = origem.compareTo(destino) > 0 ? origem : destino;
        String segundo = primeiro.equals(origem) ? destino : origem;


        if (origem.contains("@")) {
            u1 = bankRepository.findByEmailForUpdate(primeiro)
                    .orElseThrow(() -> new NotFndException("Credencial não encontrada")
                    );
            historicoDTO.setCredencialOrigem(origem);
        } else if (cpfV1.length() == 11) {

            u1 = bankRepository.findByCpfForUpdate(primeiro)
                    .orElseThrow(() -> new NotFndException("Credencial não encontrada")
                    );
            historicoDTO.setCredencialOrigem(origem);
        }

        if (destino.contains("@")) {
            u2 = bankRepository.findByEmailForUpdate(segundo)
                    .orElseThrow(() -> new NotFndException("Credencial não encontrada")
                    );
            historicoDTO.setCredencialDestino(destino);
        } else if (cpfV2.length() == 11) {

            u2 = bankRepository.findByCpfForUpdate(segundo)
                    .orElseThrow(() -> new NotFndException("Credencial não encontrada")
                    );
            historicoDTO.setCredencialDestino(destino);
        }

        UserEntity saida = primeiro.equals(origem) ? u1 : u2;
        UserEntity recebimento = segundo.equals(origem) ? u1 : u2;


        if (dto.getValorTransferencia().compareTo(saida.getLimiteDiario()) > 0) {
            throw new IllegalArgException("Valor de transferência excedeu o limite diário");
        }
        if (dto.getValorTransferencia().compareTo(saida.getSaldo()) > 0) {
            throw new IllegalArgException("Saldo insuficiente");
        }

        saida.setLimiteDiario(saida.getLimiteDiario().subtract(saldo));
        saida.setSaldo(saida.getSaldo().subtract(saldo));
        recebimento.setSaldo(recebimento.getSaldo().add(dto.getValorTransferencia()));
        historicoDTO.setValorTransferencia(saldo);

        bankRepository.saveAll(List.of(saida, recebimento));


        histService.salvarHistorico(saida, recebimento, historicoDTO);
    }


    public ResponseUserDTO buscarCliente(String credencial) {
        String valor = credencial == null ? "" : credencial.trim();
        String cpfV = valor.replaceAll("\\D", "");

        if (valor.isBlank()) {
            throw new IllegalArgException("Credenciais vazias");
        }

        UserEntity user = new UserEntity();

        if (valor.contains("@")) {
            user = bankRepository.findByEmail(valor).orElseThrow(
                    () -> new NotFndException("Email invalido")
            );
        }
        ;
        if (cpfV.length() == 11) {
            user = bankRepository.findByCpf(valor).orElseThrow(
                    () -> new NotFndException("Email invalido")
            );
        }

        return new ResponseUserDTO(user);
    }

//    PUT

    @Transactional
    public void update(UpdateUserNameDTO dto) {
        UserEntity user;

        String cpfV = dto.getCredencialVerify().replaceAll("\\D", "").trim();

        if (dto.getCredencialVerify().contains("@")) {
            user = bankRepository.findByEmail(dto.getCredencialVerify())
                    .orElseThrow(() -> new IllegalArgException("Credencial invalida"));
        } else if (cpfV.length() == 11) {
            user = bankRepository.findByCpf(dto.getCredencialVerify())
                    .orElseThrow(() -> new IllegalArgException("Credencial invalida"));
        } else {
            throw new IllegalArgException("credencial invalido");
        }

        user.setNome(dto.getNome());

        bankRepository.save(user);
    }


    public void depositarByEmail(String email, BigDecimal valor) {

        UserEntity Usuario = bankRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgException("Email inexistente")
        );

        Usuario.setSaldo(Usuario.getSaldo().add(valor));
        bankRepository.save(Usuario);
    }

    public SaqueDTO sacarByEmail(String email, BigDecimal valor) {

        UserEntity Usuario = bankRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgException("Credencial invalida"));

        if (Usuario.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgException("Saldo insuficiente");
        }

        Usuario.setSaldo(Usuario.getSaldo().subtract(valor));
        bankRepository.save(Usuario);

        return new SaqueDTO(Usuario);

    }

//    DELETE

    public void delete(Long id) {
        bankRepository.deleteById(id);
    }

    public void deleteAll() {
        bankRepository.deleteAll();
    }

}
