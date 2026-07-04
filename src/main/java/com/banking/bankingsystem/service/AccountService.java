package com.banking.bankingsystem.service;

import com.banking.bankingsystem.exception.AccountNotFoundException;
import com.banking.bankingsystem.exception.InsufficientBalanceException;
import com.banking.bankingsystem.exception.DuplicateAccountException;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.User;
import com.banking.bankingsystem.repository.UserRepository;
import com.banking.bankingsystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository){
        this.repository = repository;
    }

    public List<Account> getAllAccounts(){
        return repository.findAll();
    }

    public Account getAccounntById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with id: " + id));
    }

    public Account createAccount(Account account){

        repository.findByAccountNumber(
                        account.getAccountNumber())
                .ifPresent(existing -> {
                    throw new DuplicateAccountException(
                            account.getAccountNumber());
                });

        return repository.save(account);
    }

    public Account updateAccount(Long id, Account account){
        Account existing = getAccounntById(id);
        existing.setOwnerName(account.getOwnerName());
        existing.setBalance(account.getBalance());
        existing.setCity(account.getCity());
        return repository.save(existing);
    }

    public void deleteAccount(Long id){
        Account existing = getAccounntById(id);
        repository.delete(existing);
    }

    public List<Account> getAccountByCity(String city){
        return repository.findByCity(city);
    }

    public Account deposit(Long id, Double amount){
        Account account = getAccounntById(id);
        account.setBalance(account.getBalance() + amount);
        return repository.save(account);
    }

    public  Account withdraw(Long id, Double amount){
        Account account = getAccounntById(id);
        if(account.getBalance() < amount){
            throw new InsufficientBalanceException(
                    account.getBalance(), amount);
        }
        account.setBalance(account.getBalance() - amount);
        return repository.save(account);
    }

    @Autowired
    private UserRepository userRepository;

    public List<Account> getAccountsByUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return repository.findByUser(user);
    }

    public Account createAccountForUser(
            Account account, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        account.setUser(user);
        return repository.save(account);
    }
}
