package com.banking.bankingsystem.exception;

public class DuplicateAccountException extends RuntimeException{

    public DuplicateAccountException(String accountNumber){
        super("Account already exists with number: " + accountNumber);
    }
}
