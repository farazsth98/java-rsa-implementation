public class Run
{
	public static void main(String[] args)
	{
		RSA rsa = new RSA();
		
		System.out.println(Algorithms.extEuclid(7, 160));
		System.out.println(rsa.getCipherText());
		System.out.println(rsa.getDecryptedText());
	}
}
