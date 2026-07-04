package com.banking.bankingsystem.repository;

import com.banking.bankingsystem.model.User;
import com.banking.bankingsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    // Spring Data JPA generates SQL automatically!
    // Just write method names — Spring does the rest

    // SELECT * FROM accounts WHERE owner_name = ?
    List<Account> findByOwnerName(String ownerName);

    // SELECT * FROM accounts WHERE account_number = ?
    Optional<Account> findByAccountNumber(String accountNumber);

    //SELECT * FROM accounts WHERE city = ?
    List<Account> findByCity(String city);

    // SELECT * FROM accounts WHERE account_type = ?
    List<Account> findByAccountType(String accountType);

    //SELECT * FROM accounts WHERE balance > ?
    List<Account> findByBalanceGreaterThan(Double balance);

    List<Account> findByUser(User user);

    List<Account> findByUserId(Long userId);
}
