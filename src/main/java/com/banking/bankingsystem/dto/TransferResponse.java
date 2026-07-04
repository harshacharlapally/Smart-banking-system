package com.banking.bankingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransferResponse {
    private String message;
    private String fromAccount;
    private String toAccount;
    private Double amount;
    private Double fromNewBalance;
    private Double toNewBalance;
    private LocalDateTime timestamp;
}
