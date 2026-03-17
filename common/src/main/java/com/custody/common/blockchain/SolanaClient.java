import com.p2p.solana.rpc.RpcClient;
import com.p2p.solana.rpc.Cluster;
import com.p2p.solana.rpc.types.PublicKey;
import com.p2p.solana.rpc.types.Keypair;
import java.math.BigInteger;

public class SolanaClient implements BlockchainClient {

    private final RpcClient client;

    public SolanaClient() {
        this.client = new RpcClient(Cluster.DEVNET);
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
        long slot = client.getApi().getSlot();
        return BigInteger.valueOf(slot);
    }

    @Override
    public String getChainName() {
        return "SOLANA";
    }
}