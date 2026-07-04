package com.banking.bankingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.banking.bankingsystem.dto.TransferRequest;
import com.banking.bankingsystem.dto.TransferResponse;
import com.banking.bankingsystem.model.Transaction;
import com.banking.bankingsystem.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions",
        description = "Fund transfer and " +
        "transaction history APIs")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    //POST - Transfer funds
    //URL: POST http://localhost:8080/api/transactions/transfer
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody
            TransferRequest request) {
        TransferResponse response =
                transactionService.transferFunds(request);
        return ResponseEntity.ok(response);
    }

    //GET - Transaction history with pagination
    //URL: GET http://localhost:8080/api/transactions/history/ACC001?page=0&size=10
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<Page<Transaction>>
            getHistory(
                    @PathVariable String accountNumber,
                    @RequestParam(defaultValue = "0")
                    int page,
                    @RequestParam(defaultValue = "10")
                    int size) {
        Page<Transaction> transactions =
                transactionService.getTransactionHistory(
                        accountNumber, page, size);
        return ResponseEntity.ok(transactions);
    }
}
