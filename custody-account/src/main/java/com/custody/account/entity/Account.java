package com.custody.account.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    private String address;          // 主键
    private String chainName;        // ETH/SOLANA
    private BigDecimal balance;      // 余额
    private String status;           // ACTIVE/FROZEN
}