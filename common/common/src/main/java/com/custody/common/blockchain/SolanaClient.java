package com.custody.common.blockchain;

import com.paymennt.solana4j.SolanaClient;
import com.paymennt.solana4j.rpc.Cluster;

import java.math.BigInteger;

public class SolanaClient implements BlockchainClient {

    private final SolanaClient client;

    public SolanaClient() {
        this.client = SolanaClient.getInstance(Cluster.DEVNET);
    }

    @Override
    public String generateAddress() {
        return client.createKeyPair().getPublicKey().toBase58();
    }

    @Override
    public BigInteger getBalance(String address) throws Exception {
        return client.getBalance(address).getValue();
    }

    @Override
    public BigInteger getLatestBlockHeight() throws Exception {
        return BigInteger.valueOf(client.getSlot());
    }

    @Override
    public String getChainName() {
        return "SOLANA";
    }
}