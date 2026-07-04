package com.banking.bankingsystem.repository;

import com.banking.bankingsystem.model.Transaction;
import com.banking.bankingsystem.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccount(Account account, Pageable pageable);

    Page<Transaction> findByAccountAndType(Account account, String type, Pageable pageable);
}
