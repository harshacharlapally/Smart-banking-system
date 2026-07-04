package com.banking.bankingsystem.exception;

public class InsufficientBalanceException extends RuntimeException{

    private double availableBalance;
    private double requestedAmount;

    public  InsufficientBalanceException(double availableBalance,
                                         double requestedAmount){
        super("Insufficient balance! Available: ₹"
                + availableBalance
                + " Requested: ₹"
                + requestedAmount);
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public double getAvailableBalance(){
        return availableBalance;
    }

    public double getRequestedAmount(){
        return requestedAmount;
    }
}
