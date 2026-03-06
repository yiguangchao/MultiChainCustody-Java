package com.custody.common.blockchain;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface BlockchainClient {

    /**
     * 生成新地址
     */
    String generateAddress();

    /**
     * 查询地址余额
     */
    BigInteger getBalance(String address) throws Exception;

    /**
     * 获取最新区块高度
     */
    BigInteger getLatestBlockHeight() throws Exception;

    /**
     * 链标识（ETH, SOLANA, BTC 等）
     */
    String getChainName();
}