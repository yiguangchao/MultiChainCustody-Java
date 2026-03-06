package com.custody.common.blockchain;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class EthClient implements BlockchainClient {

    private final Web3j web3j;

    public EthClient() {
        this.web3j = Web3j.build(new HttpService("https://sepolia.infura.io/v3/InfuraKey"));
    }

    @Override
    public String generateAddress() {
        return "0x" + org.web3j.crypto.Keys.createEcKeyPair().getPublicKey().toString(16).substring(0, 40);
    }

    @Override
    public BigInteger getBalance(String address) throws Exception {
        return web3j.ethGetBalance(address, org.web3j.protocol.core.DefaultBlockParameterName.LATEST)
                .send()
                .getBalance();
    }

    @Override
    public BigInteger getLatestBlockHeight() throws Exception {
        return web3j.ethBlockNumber().send().getBlockNumber();
    }

    @Override
    public String getChainName() {
        return "ETH";
    }
}