package com.banking.bankingsystem.model;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import javax.annotation.processing.Generated;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotBlank(message = "Account number is required")
    @Column(unique = true)
    private String accountNumber;

    @NotNull(message = "Balance is required")
    @Min(value = 0,
        message = "Balance cannot be negative")
    private Double balance;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotBlank(message = "City is required")
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Transaction> transactions;
}
