import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;
import java.util.Base64;

public class TestChain {

	private List<Block> chain;
	public static int difficulty = 2;
	public static Wallet walletA;
	public static Wallet walletB;
	
	public TestChain()
	{
		this.chain = new ArrayList<Block>();
		chain.add(generateGenesis());
		
	}
	/**
	 * generates the genesis block(first block)
	 */
	private Block generateGenesis()
	{
		Block genesis = new Block("genesisBlock", new java.util.Date());
		return genesis;
	}
	/**
	 * adds a new block to the blockchain
	 * @param blk - previous block
	 */
	public void addBlock(Block blk)
	{
		Block newBlock = blk;
		blk.setPreviousHash(chain.get(chain.size()-1).getHash());
		newBlock.calculateHash();
		chain.add(newBlock);
	}
	
	/**
	 * displays the chain
	 */
	public void displayChain()
	{
		for(int i=0;i<chain.size();i++)
		{
			System.out.println("Block:" + i);
			System.out.println("TimeStemp:" + chain.get(i).getTimeStamp());
			System.out.println("PreviousHash:" + chain.get(i).getPreviousHash());
			System.out.println("Hash:" + chain.get(i).getHash());

		}
	}
	/**
	 * displays the blockchain with Json library
	 */
	public void displayChainWithJson()
	{
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(chain);		
		System.out.println(blockchainJson);
	}
	public Block getLastBlock()
	{
		return this.chain.get(chain.size()-1);
	}
	/**
	 *  Checks if the blockchain is valid. 
	 *  
	 * @return true if valid false if not valid.
	 */
	public boolean isValid()
	{
		Block currentBlock; 
		Block nextBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		for(int i=0; i < chain.size()-1; i++)
		{
			currentBlock = chain.get(i);
			nextBlock = chain.get(i+1);
			//compare registered hash and calculated hash:
			if( !currentBlock.hash.equals(currentBlock.calculateHash()))
			{
				System.out.println("Current Hashes are not equal, chain is not valid!");
				return false;
			}
			//compare previous hash and registered previous hash
			if (!currentBlock.hash.equals(nextBlock.previousHash))
			{
				System.out.println("Previous Hashes are not equal, chain is not valid!");
				return false;
			}
			//check if hash is solved
			if(!chain.get(i).hash.substring( 0, difficulty).equals(hashTarget)) 
			{
				System.out.println("block number:" + i + " hasn't been mined");
				return false;
			}
		}
		System.out.println("chain is valid");
		return true;
	}
	public List<Block> getChain() { return this.chain;}
	
	public static void main(String[] args)	{
		TestChain chain = new TestChain();
		System.out.println("Trying to Mine block 0... ");
		chain.getChain().get(0).mineBlock(2);
		
		chain.addBlock(new Block("Test", new java.util.Date()));
		System.out.println("Trying to Mine block 1... ");
		chain.getChain().get(1).mineBlock(TestChain.difficulty);
		
		chain.addBlock(new Block("Test2", new java.util.Date()));
		System.out.println("Trying to Mine block 3... ");
		chain.getChain().get(2).mineBlock(TestChain.difficulty);
		
		//chain.addBlock(new Block("Test3", new java.util.Date()));
		//System.out.println("Trying to Mine block 3... ");
		
		
		System.out.println(chain.isValid());
		//chain.displayChain();
		chain.displayChainWithJson();
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		walletA = new Wallet();
		walletB = new Wallet();
		
		System.out.println("Private and public keys:");
		System.out.println(StringUtil.getStringFromKey(walletA.getPublicKey()));
		System.out.println(StringUtil.getStringFromKey(walletA.getPrivateKey()));
		
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 5 , null);
		transaction.generatSignatureKey(walletA.getPrivateKey());
		System.out.println("Is signature verified");
		System.out.println(transaction.verifiySignature());
		
	}
}

