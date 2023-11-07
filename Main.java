import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Block {
    private int index;
    private long timestamp;
    private String data;
    private String previousHash;
    private String hash;

    public Block(int index, long timestamp, String data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String dataToHash = index + timestamp + data + previousHash;
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] bytes = sha256.digest(dataToHash.getBytes());
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    public int getIndex() {
        return index;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setData(String data) {
        this.data = data;
        this.hash = calculateHash();
    }
}

class Blockchain {
    private ArrayList<Block> blocks;

    public Blockchain() {
        blocks = new ArrayList<>();
        // Genesis block
        Block genesisBlock = new Block(0, System.currentTimeMillis(), "Genesis Block", "0");
        blocks.add(genesisBlock);
    }

    public void addBlock(String data) {
        Block previousBlock = blocks.get(blocks.size() - 1);
        Block newBlock = new Block(previousBlock.getIndex() + 1, System.currentTimeMillis(), data, previousBlock.getHash());
        blocks.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
            Block previousBlock = blocks.get(i - 1);

            // Check if hashes are valid
            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) || !currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        blockchain.addBlock("Transaction 1");
        blockchain.addBlock("Transaction 2");

        System.out.println("Blockchain is valid: " + blockchain.isChainValid());

        // Tamper with the blockchain
        blockchain.getBlocks().get(1).setData("Modified Transaction");
        System.out.println("Blockchain is valid: " + blockchain.isChainValid());
    }
}
