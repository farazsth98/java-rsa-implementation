import java.util.Random;

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
	public static int randomPrime(int a, int b)
	{
		boolean isPrime = false;
		int min = a, max = b;
		
		Random rand = new Random();
		int possiblePrime;

		// Until a prime number is found
		while (!isPrime)
		{
			// Get a random number between min and max
			possiblePrime = rand.nextInt((max - min) + 1) + min;
		}
	}
}
