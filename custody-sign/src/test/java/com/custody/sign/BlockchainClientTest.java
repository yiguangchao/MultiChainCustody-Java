package com.custody.sign;

import com.custody.common.blockchain.BlockchainClient;
import com.custody.common.blockchain.EthClient;
import com.custody.common.blockchain.SolanaClient;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BlockchainClientTest {

    @Test
    void testEthBalance() throws Exception {
        BlockchainClient client = new EthClient();
        String address = client.generateAddress();
        System.out.println("ETH Address: " + address);
        BigInteger balance = client.getBalance("0x742d35Cc6634C0532925a3b844Bc454e4438f44e");
        System.out.println("Balance: " + balance);
        assertNotNull(balance);
    }

    @Test
    void testSolanaBalance() throws Exception {
        BlockchainClient client = new SolanaClient();
        String address = client.generateAddress();
        System.out.println("Solana Address: " + address);
        BigInteger balance = client.getBalance("So11111111111111111111111111111111111111112");
        System.out.println("Balance: " + balance);
        assertNotNull(balance);
    }
}