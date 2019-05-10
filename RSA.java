/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: This file contains the RSA class. This class is used to represent
 *			 an RSA object. It calculates the required values and stores them,
 *			 then encrypts an input file. It then decrypts the encrypted file.
 *			 Everything is output to separate files.
 *	Last modified: 11/05/2019
 */

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class RSA
{
	// The two prime numbers between 1000 and 10000
	private long p;
	private long q;

	// n = p*q
	// phi = (p-1)*(q-1)
	private long n;
	private long phi;

	private long publicKey;
	private long privateKey;

	private final String outputFileName = "decrypted.txt";

	public RSA(String filename)
	{
		// Select p and q
		p = Algorithms.randomPrime(1000, 10000);
		q = Algorithms.randomPrime(1000, 10000);

		// Calculate n and phi
		n = p*q;
		phi = (p-1) * (q-1);

		// Find e for the public key and use it to find d
		// for the private key
		publicKey = this.findPublicKey();
		privateKey = Algorithms.extEuclid(publicKey, phi);

		try
		{
			this.encryptFile(filename);
			this.decryptFile("encrypted.txt");
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// Encryption and decryption methods
	private long encrypt(long c)
	{
		// This calculates (c^publicKey) mod n
		return Algorithms.fast_mod_exp(c, publicKey, n);
	}

	private long decrypt(long d)
	{
		// This calculates (d^privateKey) mod n
		return Algorithms.fast_mod_exp(d, privateKey, n);
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

	// This method encrypts the file given by the filename
	// It writes the encrypted output to encrypted.txt
	private void encryptFile(String filename) throws IOException
	{
		File input = new File(filename);
		File encryptedOutput = new File("encrypted.txt");
		PrintWriter pwEncrypted;
		Scanner sc;

		String line;
		long c;

		sc = new Scanner(input);
		pwEncrypted = new PrintWriter(encryptedOutput);

		while (sc.hasNextLine())
		{
			line = sc.nextLine();

			// Ignore empty lines and just print a new line
			if (line.equals(""))
				pwEncrypted.println();
			else
			{
				for (int i = 0; i < line.length(); i++)
				{
					// Convert each char to a long and encrypt this long
					c = line.charAt(i);
					c = this.encrypt(c);

					// Output each long value to the file, separated by " "
					pwEncrypted.print(c + " ");
				}

				pwEncrypted.println();
			}
		}

		pwEncrypted.close();

		System.out.println("Encrypted output written to encrypted.txt");
	}

	// This method decrypts the file given by filename
	// It writes the decrypted output to decrypted.txt
	private void decryptFile(String filename) throws IOException
	{
		File input = new File(filename);
		File decryptedOutput = new File("decrypted.txt");
		PrintWriter pwDecrypted;
		Scanner sc;

		String line;
		String[] longArray;
		long d;
		char c;

		sc = new Scanner(input);
		pwDecrypted = new PrintWriter(decryptedOutput);

		while (sc.hasNextLine())
		{
			line = sc.nextLine().trim();

			// If an empty line is found, do nothing
			if (line.equals(""));

			// Else, split the line on a space character, and read each
			// array index as a long and decrypt it, then output the
			// the decrypted value as a char to the output file
			else
			{
				longArray = line.split(" ");

				for (int i = 0; i < longArray.length; i++)
				{
					// Convert each char to a long and decrypt this long
					d = Long.parseLong(longArray[i]);
					d = this.decrypt(d);
				
					// The long value is ensured to fit into a char as that is what
					// it originally was
					c = (char) d;
					pwDecrypted.print(c);
				}
			}

			pwDecrypted.println();
		}

		pwDecrypted.close();

		System.out.println("Decrypted output written to decrypted.txt");
	}
}
