package com.banking.bankingsystem.controller;

import org.springframework.security.core.Authentication;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management",
        description = "APIs for managing bank accounts")
public class AccountController {
    private final AccountService service;

    @Autowired
    public  AccountController(AccountService service){
        this.service =service;
    }

    @Operation(
            summary = "Get all accounts",
            description = "Returns list of all bank accounts")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
            description = "Successfully retrieved accounts"),
            @ApiResponse(responseCode = "401",
            description = "Unauthorized - login required")
    })

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
        List<Account> accounts = service.getAllAccounts();
        return  ResponseEntity.ok(accounts);
    }

    @Operation(
            summary = "Get accounts by ID",
            description = "Return single account details")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Account found"),
            @ApiResponse(responseCode = "404",
                    description = "Account not found")
    })

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @Parameter(description = "ID of account to retrieve")
            @PathVariable Long id){
        Account account = service.getAccounntById(id);
        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Create new account",
            description = "Create a new bank account " +
                    "for the logged in user")

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody Account account,
            Authentication authentication){

        String email = authentication.getName();
        Account created = service.createAccountForUser(account,email);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}/balance")
    public  ResponseEntity<Account> updateAccount(
            @PathVariable Long id, @Valid @RequestBody Account account){
        Account updated = service.updateAccount(id, account);
        return  ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete account",
            description = "Admin only - permanently " +
                    "delete an account"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(
            @PathVariable Long id){
        service.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully");
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Account>> getByCity(
            @PathVariable String city){
        List<Account> accounts = service.getAccountByCity(city);
        return ResponseEntity.ok(accounts);
    }

    @Operation(
            summary = "Deposit money",
            description = "Add money to account balance")

    @PutMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(
            @Parameter(description = "Account ID")
            @PathVariable Long id,
            @Parameter(description = "Amount to deposit")
            @RequestParam Double amount){
        Account account = service.deposit(id, amount);
        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Withdraw money",
            description = "Withdraw money from account, " +
                    "fails if insufficient balance")
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable Long id,
            @RequestParam double amount){
        Account account = service.withdraw(id, amount);
        return ResponseEntity.ok(account);
    }
}
