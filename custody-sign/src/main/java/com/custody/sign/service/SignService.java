package com.custody.sign.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;
import org.bouncycastle.crypto.util.DigestFactory;
import java.math.BigInteger;
@Slf4j
@Service
public class SignService {

    private static final String CURVE_NAME = "secp256k1";

    public String signEcdsa(String message, String privateKeyHex) throws Exception {
        // 1. 解析 raw 私钥
        byte[] privKeyBytes = Hex.decode(privateKeyHex);
        if (privKeyBytes.length != 32) {
            throw new IllegalArgumentException("Private key must be 32 bytes (64 hex chars)");
        }
        BigInteger privKey = new BigInteger(1, privKeyBytes);  // 正数表示

        // 2. 获取 secp256k1 曲线参数 (BouncyCastle)
        X9ECParameters curveParams = SECNamedCurves.getByName(CURVE_NAME);
        ECDomainParameters domain = new ECDomainParameters(
                curveParams.getCurve(),
                curveParams.getG(),
                curveParams.getN(),
                curveParams.getH(),
                curveParams.getSeed()
        );

        // 3. 创建 ECDSA 签名器
        ECDSASigner signer = new ECDSASigner();
        signer.init(true, new ECPrivateKeyParameters(privKey, domain));
        byte[] messageBytes = message.getBytes("UTF-8");
        // 4. 计算消息哈希
        SHA256Digest digest = new SHA256Digest();
        digest.update(messageBytes, 0, messageBytes.length);

        byte[] hash = new byte[digest.getDigestSize()];  // 32 字节
        digest.doFinal(hash, 0);

        // 5. 签名
        BigInteger[] signature = signer.generateSignature(hash);

        // 6. 转换为可读格式
        byte[] r = signature[0].toByteArray();
        byte[] s = signature[1].toByteArray();

        byte[] rPadded = padTo32(r);
        byte[] sPadded = padTo32(s);

        byte[] sigBytes = new byte[64];
        System.arraycopy(rPadded, 0, sigBytes, 0, 32);
        System.arraycopy(sPadded, 0, sigBytes, 32, 32);

        return Hex.toHexString(sigBytes);
    }

    private byte[] padTo32(byte[] bytes) {
        if (bytes.length == 32) return bytes;
        if (bytes.length > 32) throw new IllegalStateException("Too long");
        byte[] padded = new byte[32];
        System.arraycopy(bytes, 0, padded, 32 - bytes.length, bytes.length);
        return padded;
    }
}