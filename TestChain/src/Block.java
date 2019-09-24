import java.util.Date;

public class Block {
	
	public String hash;
	public String previousHash;
	private String data;
	private Date timeStamp; //as number of milliseconds since 1/1/1970.
	private int nonce;
	

	/**
	 * 
	 * //Block Constructor.
	 */
	public Block(String data, Date timestamp) {
		this.data = data;
		//this.previousHash = previousHash;
		this.timeStamp = timestamp;
		this.hash = calculateHash();
		this.nonce=0;
	}
	
	/**
	 * A function to calculate the hash from the data of the block
	 */
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256( 
				this.previousHash +
				this.timeStamp +
				Integer.toString(nonce) +
				this.data 
				);
		return calculatedhash;
	}
	
	/**
	 * A function to mine blocks
	 */
	public void mineBlock(int difficulty)
	{
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		while(!(hash.substring(0, difficulty).equals(target))) {
			this.nonce ++;
			this.hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
