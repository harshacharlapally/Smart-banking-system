package com.banking.bankingsystem.service;

import com.banking.bankingsystem.dto.TransferRequest;
import com.banking.bankingsystem.dto.TransferResponse;
import com.banking.bankingsystem.exception.AccountNotFoundException;
import com.banking.bankingsystem.exception.InsufficientBalanceException;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.Transaction;
import com.banking.bankingsystem.repository.*;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public TransferResponse transferFunds(TransferRequest request){

        //step - 1 Find both accounts
        Account fromAccount = accountRepository.findByAccountNumber(
                request.getFromAccountNumber()).orElseThrow(() ->
                new AccountNotFoundException(
                        "Account not found: " +
                                request.getFromAccountNumber()));

        Account toAccount = accountRepository.findByAccountNumber(
                request.getToAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found: " +
                                        request.getToAccountNumber()));

        //step - 2 Check sufficient balance
        if(fromAccount.getBalance() <
                request.getAmount()) {
            throw new InsufficientBalanceException(
                    fromAccount.getBalance(), request.getAmount());
        }

        //step - 3 Deduct from sender
        fromAccount.setBalance(
                fromAccount.getBalance() - request.getAmount());
        accountRepository.save(fromAccount);

        //step - 4 Add to receiver
       // fromAccount.setBalance(
         //       fromAccount.getBalance() - request.getAmount());
        //accountRepository.save(fromAccount);

        //TEST ROLLBACK
        // if(true){
         //   throw new RuntimeException("Test rollback");
        //}

        toAccount.setBalance(
                toAccount.getBalance() + request.getAmount());
        accountRepository.save(toAccount);

        //step - 5 Record DEBIT transaction
        Transaction debitTxn = Transaction.builder()
                .amount(request.getAmount())
                .type("DEBIT")
                .description("Transfer to " +
                        request.getToAccountNumber()+
                        " = " + request.getDescription())
                .timeStamp(LocalDateTime.now())
                .balanceAfter(fromAccount.getBalance())
                .account(fromAccount)
                .build();
        transactionRepository.save(debitTxn);

        //step - 6 Record CREDIT transaction
        Transaction creditTxn = Transaction.builder()
                .amount(request.getAmount())
                .type("CREDIT")
                .description("Transfer from " +
                        request.getFromAccountNumber() +
                        " = " + request.getDescription())
                .timeStamp(LocalDateTime.now())
                .balanceAfter(toAccount.getBalance())
                .account(toAccount)
                .build();
        transactionRepository.save(creditTxn);

        //step - 7 Send Email Alerts
        if(fromAccount.getUser() != null){
            emailService.sendTransactionAlert(
                    fromAccount.getUser().getEmail(),
                    fromAccount.getUser().getName(),
                    "DEBIT",
                    request.getAmount(),
                    fromAccount.getBalance(),
                    fromAccount.getAccountNumber()
            );
        }

        if(toAccount.getUser() != null){
            emailService.sendTransactionAlert(
                    toAccount.getUser().getEmail(),
                    toAccount.getUser().getName(),
                    "CREDIT",
                    request.getAmount(),
                    toAccount.getBalance(),
                    toAccount.getAccountNumber()

            );
        }

        //step - 8 Return Response
        return new TransferResponse(
                "Transfer successful!",
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getAmount(),
                fromAccount.getBalance(),
                toAccount.getBalance(),
                LocalDateTime.now()
        );
    }

    //Get transaction history with pagination
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionHistory(
            String accountNumber,
            int page,
            int size) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found: " +
                                        accountNumber));

        //Pageable - page number, size, sort by timestamp DESC
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("timeStamp").descending()
        );

        return transactionRepository.findByAccount(account, pageable);
    }
}
