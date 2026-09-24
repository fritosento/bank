package com.taskmathproject.bank.DataBase.Repository;

import com.taskmathproject.bank.DataBase.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByNome(String name);
    Optional<UserEntity> findByCpf(String cpf);
    Optional<UserEntity> findByEmail(String email);

}
