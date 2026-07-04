package com.banking.bankingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description =
    "Request body for transferring funds " +
    "between two accounts")
public class TransferRequest {

    @Schema(description = "Account number sending money",
    example = "ACC001")
    @NotBlank(message = "From account number required")
    private String fromAccountNumber;

    @Schema(description = "Account number receiving money",
    example = "ACC002")
    @NotBlank(message = "To account number required")
    private String toAccountNumber;

    @Schema(description = "Amount to transfer",
    example = "5000")
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @Schema(description = "Transfer description",
    example = "Rent payment")
    private String description;
}
