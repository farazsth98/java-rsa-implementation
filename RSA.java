import java.math.BigInteger;

public class RSA
{
	private long p;
	private long q;
	private long n;
	private long phi;

	private long publicKey;
	private long privateKey;

	private long plaintext;
	private long ciphertext;
	private long decrypted;

	public RSA()
	{
		// p and q are two random prime numbers between 1000 and 10000
		//p = Algorithms.randomPrime(1000, 10000);
		//q = Algorithms.randomPrime(1000, 10000);
		p = 17;
		q = 11;

		n = p*q;
		phi = (p-1) * (q-1);

		//publicKey = this.findPublicKey();
		publicKey = 7;
		privateKey = Algorithms.extEuclid(publicKey, phi);

		plaintext = 88;
		this.encrypt();
		this.decrypt();
	}

	public long getCipherText()
	{
		return this.ciphertext;
	}

	public long getDecryptedText()
	{
		return this.decrypted;
	}

	// Encryption and decryption methods
	private void encrypt()
	{
		System.out.println("PublicKey = " + publicKey);
		ciphertext = Algorithms.fast_mod_exp(plaintext, publicKey, n);
	}

	private void decrypt()
	{
		decrypted = Algorithms.fast_mod_exp(ciphertext, privateKey, n);
	}

	// To find the public key, we test values 11, 13, 17
	// The first value we find that is co-prime to phi, we will use
	private long findPublicKey()
	{
		long publicKey = 0;

		// One of these is guaranteed to go through
		if (Algorithms.euclid(11, phi) == 1)
			publicKey = 11;
		else if (Algorithms.euclid(13, phi) == 1)
			publicKey = 13;
		else if (Algorithms.euclid(17, phi) == 1)
			publicKey = 17;

		return publicKey;
	}
}
