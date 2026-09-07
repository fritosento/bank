package com.taskmathproject.bank.Repository;

import com.taskmathproject.bank.Model.TransfModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistRepository extends JpaRepository<TransfModel,Long> {
    List<TransfModel> findByOrigemIdOrDestinoId(Long origemId, Long oestinoId);
}
