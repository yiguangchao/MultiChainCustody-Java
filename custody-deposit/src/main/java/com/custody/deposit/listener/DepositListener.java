package com.custody.deposit.listener;

import com.custody.deposit.event.DepositEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositListener {

    private final AccountService accountService;

    @KafkaListener(topics = "deposit-events", groupId = "custody-group")
    public void handleDeposit(DepositEvent event) {
        // 更新账户余额
        accountService.updateBalance(
                event.getAddress(),
                event.getChainName(),
                event.getAmount()
        );
        System.out.println("Deposit processed: " + event);
    }
}