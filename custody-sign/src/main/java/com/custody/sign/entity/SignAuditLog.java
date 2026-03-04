package com.custody.sign.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "sign_audit_logs")
@Data
public class SignAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;           // 唯一请求 ID
    private String messageHash;         // 消息哈希
    private String signerCount;         // 使用了几个分片
    private String signatureHex;        // 最终签名
    private LocalDateTime signTime;
    private String status;              // SUCCESS / FAILED
    private String errorMessage;
}