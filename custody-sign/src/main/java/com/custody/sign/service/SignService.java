package com.custody.sign.service;

import com.codahale.shamir.Scheme;
import com.custody.sign.entity.SignAuditLog;
import com.custody.sign.repository.SignAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SignService {

    private final SignAuditLogRepository auditRepository;

    private static final String CURVE_NAME = "secp256k1";
    private static final int TOTAL_SHARES = 3;
    private static final int THRESHOLD = 2;

    // 模拟私钥分片（实际生产中分片存储在不同地方）
    private final Map<Integer, byte[]> shareMap = new HashMap<>();

    // 初始化模拟分片（仅 demo 用，实际从配置/数据库加载）
    public void initDemoShares(String rawPrivateKeyHex) {
        byte[] masterKey = Hex.decode(rawPrivateKeyHex);
        SecureRandom random = new SecureRandom();
        Scheme scheme = new Scheme(random, TOTAL_SHARES, THRESHOLD);
        Map<Integer, byte[]> shares = scheme.split(masterKey);
        shareMap.putAll(shares);
    }

    public String thresholdSignEcdsa(String message, List<Integer> shareIds) throws Exception {
        if (shareIds.size() < THRESHOLD) {
            throw new IllegalArgumentException("Insufficient shares: need at least " + THRESHOLD);
        }

        // 1. 计算消息哈希
        byte[] messageBytes = message.getBytes("UTF-8");
        SHA256Digest digest = new SHA256Digest();
        digest.update(messageBytes, 0, messageBytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);

        // 2. 收集分片并重构私钥（demo 简化，实际用 Lagrange 插值）
        byte[] reconstructed = reconstructPrivateKey(shareIds);

        // 3. secp256k1 参数
        X9ECParameters curveParams = SECNamedCurves.getByName(CURVE_NAME);
        ECDomainParameters domain = new ECDomainParameters(
                curveParams.getCurve(),
                curveParams.getG(),
                curveParams.getN(),
                curveParams.getH(),
                curveParams.getSeed()
        );

        // 4. 签名
        ECDSASigner signer = new ECDSASigner();
        signer.init(true, new ECPrivateKeyParameters(new BigInteger(1, reconstructed), domain));
        BigInteger[] sig = signer.generateSignature(hash);

        // 5. 格式化 r + s
        byte[] r = padTo32(sig[0].toByteArray());
        byte[] s = padTo32(sig[1].toByteArray());
        byte[] sigBytes = new byte[64];
        System.arraycopy(r, 0, sigBytes, 0, 32);
        System.arraycopy(s, 0, sigBytes, 32, 32);

        String signatureHex = Hex.toHexString(sigBytes);

        // 6. 记录审计日志
        SignAuditLog log = new SignAuditLog();
        log.setRequestId(UUID.randomUUID().toString());
        log.setMessageHash(Hex.toHexString(hash));
        log.setSignerCount(shareIds.size() + "/" + TOTAL_SHARES);
        log.setSignatureHex(signatureHex);
        log.setSignTime(LocalDateTime.now());
        log.setStatus("SUCCESS");
        auditRepository.save(log);

        return signatureHex;
    }

    // 模拟重构私钥（实际用 Shamir combine）
    private byte[] reconstructPrivateKey(List<Integer> shareIds) {
        SecureRandom random = new SecureRandom();
        Scheme scheme = new Scheme(random, TOTAL_SHARES, THRESHOLD);
        Map<Integer, byte[]> selectedShares = new HashMap<>();
        for (int id : shareIds) {
            if (shareMap.containsKey(id)) {
                selectedShares.put(id, shareMap.get(id));
            }
        }
        return scheme.join(selectedShares);
    }

    private byte[] padTo32(byte[] bytes) {
        if (bytes.length == 32) return bytes;
        byte[] result = new byte[32];
        int offset = 32 - Math.min(bytes.length, 32);
        System.arraycopy(bytes, Math.max(0, bytes.length - 32), result, offset, Math.min(bytes.length, 32));
        return result;
    }
}