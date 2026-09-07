package com.taskmathproject.bank.Repository;

import com.taskmathproject.bank.Model.BankModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BankRepository extends JpaRepository<BankModel,Long> {}
