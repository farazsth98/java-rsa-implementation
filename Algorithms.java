import java.util.Random;
import java.math.BigInteger;

public class Algorithms
{
	// This method uses the Euclidean algorithm to return the
	// greatest common denominator between the imported a and b
	// integers
	public static int euclid(int a, int b)
	{
		if (b == 0) return a;
		else return euclid(b, a % b);
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
	// Step 4: Perform this step 25 times to gain sufficient confidence
	// 		   that r is a prime number
	private static boolean isPrime(long p)
	{
		// Handle base cases
		if (p == 2) return true; // 2 is prime
		else if (p % 2 == 0) return false; // Even numbers are not prime

		Random rand = new Random();
		int a; // Random number less than n but > 1
		
		// According to lecture 6 slide 25, running this algorithm with
		// 25 iterations should be enough, therefore k=25 iterations
		int k = 25;

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

		// I use the BigInteger class here because the math here has a couple
		// steps in between that result in really large numbers that might not
		// be able to be stored in long variables
		BigInteger bigA, bigR;
		BigInteger bigP = BigInteger.valueOf(p);
		int exponent;

		// Iterate over k times
		for (int i = 0; i < k; i++)
		{
			// Set a to a random number between 2 and p
			// A random number between a given inclusive min and
			// exclusive max is given by
			// rand.nextInt((max - min) + 1) + min
			a = rand.nextInt(((int) p - 2) + 1) + 2;
			bigA = BigInteger.valueOf(a);

			// exponent is the exponent that a is raised to the power of
			// In this case, it is ((p-1)/2)
			exponent = (((int) p - 1) / 2);

			// r = a ^ ((p-1)/2)
			bigR = bigA.pow(exponent).mod(bigP);	
			r = bigR.longValue();
			
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
}
