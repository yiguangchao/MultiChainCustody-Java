package com.custody.deposit.event;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositEvent {
    private String address;
    private String chainName;
    private BigDecimal amount;
    private String txHash;
}