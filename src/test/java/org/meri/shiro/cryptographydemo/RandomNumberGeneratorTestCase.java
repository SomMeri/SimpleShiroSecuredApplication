package org.meri.shiro.cryptographydemo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

public class RandomNumberGeneratorTestCase extends CryptoTestCase {

	@Test
	public void testRandomWithoutSeed() {
		//create random generators
		RandomNumberGenerator firstGenerator = new SecureRandomNumberGenerator();
		RandomNumberGenerator secondGenerator = new SecureRandomNumberGenerator();
		
		//generate random bytes
		ByteSource firstRandomBytes = firstGenerator.nextBytes();
		ByteSource secondRandomBytes = secondGenerator.nextBytes();
	
		//compare random bytes
		assertByteSourcesNotSame(firstRandomBytes, secondRandomBytes);

		//print it to the console
		System.out.println("Two different random arrays: ");
		print(firstRandomBytes, "First random array: ");
		print(secondRandomBytes, "Second random array: ");
	}

	@Test
	public void testRandomWithSeed() {
		byte[] seed = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		
		//create and initialize first random generator
		SecureRandomNumberGenerator firstGenerator = new SecureRandomNumberGenerator();
		firstGenerator.setSeed(seed);
		firstGenerator.setDefaultNextBytesSize(20);

		//create and initialize second random generator
		SecureRandomNumberGenerator secondGenerator = new SecureRandomNumberGenerator();
		secondGenerator.setSeed(seed);
		secondGenerator.setDefaultNextBytesSize(20);

		//generate random bytes
		ByteSource firstRandomBytes = firstGenerator.nextBytes();
		ByteSource secondRandomBytes = secondGenerator.nextBytes();
	
		//compare random arrays
		assertByteSourcesEquals(firstRandomBytes, secondRandomBytes);
		
		//print it to the console
		System.out.println("Two equal random arrays: ");
		print(firstRandomBytes, "First random array: ");
		print(secondRandomBytes, "Second random array: ");

		//following nextBytes they are also the same
		ByteSource firstNext = firstGenerator.nextBytes();
		ByteSource secondNext = secondGenerator.nextBytes();

		//compare random arrays
		assertByteSourcesEquals(firstRandomBytes, secondRandomBytes);

		//compare against expected values
		byte[] expectedRandom = {-116, -31, 67, 27, 13, -26, -38, 96, 122, 31, -67, 73, -52, -4, -22, 26, 18, 22, -124, -24};
		assertArrayEquals(expectedRandom, firstNext.getBytes());
		assertEquals("8ce1431b0de6da607a1fbd49ccfcea1a121684e8", firstNext.toHex());
		assertEquals("jOFDGw3m2mB6H71JzPzqGhIWhOg=", firstNext.toBase64());

		System.out.println("Another equal random arrays: ");
		print(firstNext, "First random array: ");
		print(secondNext, "Second random array: ");
	}

}
