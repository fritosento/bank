package com.taskmathproject.bank.DataBase.Repository;

import com.taskmathproject.bank.DataBase.Model.UserEntityHistoric;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistRepository extends JpaRepository<UserEntityHistoric,Long> {
    List<UserEntityHistoric> findByOrigemCredencialOrDestinoCredencial(String origem, String destino, Sort sort);
}
