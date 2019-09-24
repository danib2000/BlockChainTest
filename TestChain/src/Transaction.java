import java.security.*;
import java.util.ArrayList;

public class Transaction {

	private String transactionId; // this is also the hash of the transaction.
	private PublicKey sender; // senders address/public key.
	private PublicKey reciepient; // Recipients address/public key.
	private float value;
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	
	private ArrayList<TransactionInput> inputs;
	private ArrayList<TransactionOutput> outputs;
	
	private static int sequence  = 0; // a rough count of how many transactions have been generated.
	
	public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs)
	{
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
		
	}
	
	/**
	 * This Calculates the transaction hash (which will be used as its Id)
	 * @return transaction hash
	 */
	private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.applySha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
			);
	}
	/**
	 * generates the signature key from the sender recipient and the value. 
	 * also signs all the data we don't wish to be tampered with.
	 * @param key the private key of the wallet
	 */
	public void generatSignatureKey(PrivateKey key)
	{
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
		signature = StringUtil.applyECDSASig(key,data);		
	}
	/**
	 * Verifies the data we signed hasn't been tampered with
	 * @return true if everything is valid, else false
	 */
	public boolean verifiySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
		return StringUtil.verifyECDSASig(sender, data, signature);
	}
	}
	

