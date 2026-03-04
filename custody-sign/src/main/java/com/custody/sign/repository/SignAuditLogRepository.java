package com.custody.sign.repository;

import com.custody.sign.entity.SignAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignAuditLogRepository extends JpaRepository<SignAuditLog, Long> {
}