import com.p2p.solana.rpc.RpcClient;
import com.p2p.solana.rpc.Cluster;
import com.p2p.solana.rpc.types.PublicKey;
import com.p2p.solana.rpc.types.Keypair;  // 用于生成密钥对
import java.math.BigInteger;

public class SolanaClient implements BlockchainClient {

    private final RpcClient client;

    public SolanaClient() {
        // 正确初始化：用 Cluster.DEVNET 或自定义 URL
        this.client = new RpcClient(Cluster.DEVNET);
        // 或自定义 RPC: new RpcClient("https://api.devnet.solana.com");
    }

    @Override
    public String generateAddress() {
        Keypair keypair = Keypair.generate();
        return keypair.getPublicKey().toBase58();
    }

    @Override
    public BigInteger getBalance(String address) throws Exception {
        PublicKey pubKey = new PublicKey(address);
        long balanceLamports = client.getApi().getBalance(pubKey).getValue();
        return BigInteger.valueOf(balanceLamports);
    }

    @Override
    public BigInteger getLatestBlockHeight() throws Exception {
        // getSlot() 返回当前 slot（Solana 的区块高度等价物）
        long slot = client.getApi().getSlot();
        return BigInteger.valueOf(slot);
    }

    @Override
    public String getChainName() {
        return "SOLANA";
    }
}