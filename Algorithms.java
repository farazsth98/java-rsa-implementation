/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: This file contains all the mathematical algorithms required for
 *			 RSA encryption. Every algorithm is described using comments below.
 *	Last modified: 11/05/2019
 */

import java.util.Random;

/* Note: The text book that I refer to throughout all this code in various
 * places is the following:
 * Cryptography and Network Security: Principles and Practice (6th edition)
 */

public class Algorithms
{
	// This method uses the Euclidean algorithm to return the
	// greatest common denominator between the imported a and b
	// integers
	public static long euclid(long a, long b)
	{
		long temp = 0;

		while (b != 0)
		{
			temp = b;
			b = a % b;
			a = temp;
		}

		return a;
	}

	// This method uses the Extended Euclidean Algorithm to return
	// the Multiplicative Modular Inverse of a with modulo n
	public static long extEuclid(long a, long n)
	{
		long temp = 0;
		long quotient; // The quotient is stored each stage
		long x0 = 1, y0 = 0; // Initial values of x and y
		long x1 = 0, y1 = 1; // Secondary values of x and y
		long tempX, tempY; // Used as intermediaries to swap x0,y0 with x1,y1

		// We also have to store the original value of n in case
		// x0 turns out to be a negative number
		long origN = n;

		// When n = 0, it means we have found the gcd
		while (n != 0)
		{
			// Steps for the normal Euclidean algorithm
			temp = n;
			quotient = a / n;
			n = a % n;
			a = temp;
			
			// Extended Euclidean Algorithm requires us to update the x and y
			// values with each step as well
			// Refer to the textbook to understand why each step does what
			// it does
			tempX = x1;
			tempY = y1;
			x1 = x0 - quotient*x1;
			y1 = y0 - quotient*y1;
			x0 = tempX;
			y0 = tempY;
		}

		// If x0 ends up being negative at this stage, we have to subtract
		// |x0| from the original value of n to get the modular multiplcative
		// inverse.
		//
		// If x0 is positive, then x0 is the modular multiplicative inverse.
		if (x0 < 0) x0 = origN + x0;

		return x0;
	}

	// This method does Fast Modular Exponentiation, which is used in
	// encryption and decryption.
	// The reason this method is used is because an intermediate step in
	// both encryption and decryption creates a VERY large number that is
	// very difficult to store in a variable.
	//
	// This implementation is taken from the book:
	// Cryptography and Network Security: Principles and Practice (6th edition)
	// Page 269, Figure 9.8
	public static long fast_mod_exp(long base, long exponent, long modulus)
	{
		// Convert the exponent into an array of bits
		long[] bits = longToBinary(exponent);

		long c = 0, f = 1;

		for (int i = 0; i < bits.length; i++)
		{
			c = 2*c;
			f = (f * f) % modulus;

			if (bits[i] == 1)
			{
				c++;
				f = (f * base) % modulus;
			}
		}

		return f;
	}

	// This method uses the Lehmann Algorithm to pick a random
	// prime number between the imported a and b
	public static long randomPrime(int a, int b)
	{
		boolean primeFound = false;
		int min = a, max = b;

		Random rand = new Random();
		long possiblePrime = 0;

		// Until a prime number is found
		while (!primeFound)
		{
			// Get a random number between min and max
			possiblePrime = rand.nextInt((max - min) + 1) + min;

			if (isPrime(possiblePrime))
				primeFound = true;
		}

		return possiblePrime;
	}

	// Uses the Lehmann algorithm to check if a p is a prime number
	//
	// Step 1: Select a value `a` that is less than `p`
	// Step 2: Find r = a^((p-1)/2) mod p
	// Step 3: If r != 1 and r-p != -1, then r is not a prime number
	// 		   Else, r is probably a prime number
	// Step 4: Perform this step 100 times to gain sufficient confidence
	// 		   that r is a prime number
	private static boolean isPrime(long p)
	{
		// Handle base cases
		if (p == 2) return true; // 2 is prime
		else if (p % 2 == 0) return false; // Even numbers are not prime

		Random rand = new Random();
		int a; // Random number less than n but > 1
		
		// According to my tutor Antoni, running this algorithm with
		// 100 iterations should be enough, therefore k=100 iterations
		int k = 100;

		// Variables required for the Lehmann Primality Test
		// as according to its description in Question 3 of Lab 2
		long r;

		// This boolean is required because the description mentioned above
		// also states that as long as every calculation equals 1 or -1 BUT
		// does not always equal 1, then the primality probability is 1/(2^k)
		//
		// Therefore, if none of the iterations result in r = -1, then I return
		// false
		boolean negativeOneFound = false;

		// The exponent = ((p-1)/2)
		int exponent;

		// Iterate over k times
		for (int i = 0; i < k; i++)
		{
			// Set a to a random number between 2 and p
			// A random number between a given inclusive min and
			// exclusive max is given by
			// rand.nextInt((max - min) + 1) + min
			a = rand.nextInt(((int) p - 2) + 1) + 2;

			// exponent is the exponent that a is raised to the power of
			// In this case, it is ((p-1)/2)
			exponent = (((int) p - 1) / 2);

			// r = (a ^ ((p-1)/2)) mod p
			r = fast_mod_exp(a, exponent, p);
			
			// If r is not 1 or -1, then do r-p
			// If r-p is not equal to -1, then return false
			//
			// The reason (r-p) is checked is because the mod function will
			// return either 1 or (p-1) initially, both of which mean that
			// p is probably prime. this is why we do r-p to get, which will
			// return -1 ONLY IF the mod function returns (p-1)
			if (r != 1 && r != -1)
			{
				r = r-p;
				if (r != -1) return false;
			}
			
			// Found a -1. The reason this is needed is explained at the
			// the variable declaration for this variable
			if (r == -1) negativeOneFound = true;
		}

		// If the algorithm reaches here without finding a single r = -1,
		// then its probably not prime
		if (!negativeOneFound) return false;

		// Else it is a prime
		else return true;
	}

	// This method converts the imported long x into a binary representation
	// that is stored in a long[] array where each index is a bit
	// Adapted from my own implementation of SDES from assignment one
	private static long[] longToBinary(long x)
	{
		String binaryString = Long.toBinaryString(x);
		long[] bits = new long[binaryString.length()];

		for (int i = 0; i < binaryString.length(); i++)
		{
			bits[i] = Character.getNumericValue(binaryString.charAt(i));
		}

		return bits;
	}

	// This method converts the imported long[] array of bits into a base 10
	// long value
	// Adapted from my own implementation of SDES from assignment one
	private static long binaryToLong(long[] bits)
	{
		StringBuilder binary = new StringBuilder();
		for (int i = 0; i < bits.length; i++)
		{
			binary.append(bits[i]);
		}

		return Long.parseLong(binary.toString(), 2);
	}
}
