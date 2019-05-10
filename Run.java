/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: Contains the main method for my RSA encryption and decryption
 *			 program.
 *	Last modified: 11/05/2019
 */

import java.util.Scanner;

public class Run
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter file to encrypt: ");
		String filename = sc.nextLine();

		RSA rsa = new RSA(filename);
	}
}
