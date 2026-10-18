package com.taskmathproject.bank.DataBase.Repository;

import com.taskmathproject.bank.DataBase.Model.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByNomeIgnoreCaseContaining(String name);
    Optional<UserEntity> findByCpf(String cpf);
    Optional<UserEntity> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UserEntity u where u.email = :email")
    Optional<UserEntity> findByEmailForUpdate(@Param("email") String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UserEntity u where u.cpf = :cpf")
    Optional<UserEntity> findByCpfForUpdate(@Param("cpf") String cpf);
}