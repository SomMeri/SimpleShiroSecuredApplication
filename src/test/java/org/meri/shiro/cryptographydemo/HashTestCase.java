package org.meri.shiro.cryptographydemo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;

public class HashTestCase extends CryptoTestCase {

	@Test
	public void testMd5Hash() {
		Hash hash = new Md5Hash("Hello Md5");
		
		byte[] expectedHash = {-7, 64, 38, 26, 91, 99, 33, 9, 37, 50, -22, -112, -99, 57, 115, -64};
		assertArrayEquals(expectedHash, hash.getBytes());
		assertEquals("f940261a5b6321092532ea909d3973c0", hash.toHex());
		assertEquals("+UAmGltjIQklMuqQnTlzwA==", hash.toBase64());

		print(hash, "Md5 with no salt iterations of 'Hello Md5': ");
	}

	@Test
	public void testIterationsSha256Hash() {
		byte[] salt = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

		Hash hash = new Sha256Hash("Hello Sha256", salt, 10);
		
		byte[] expectedHash = {24, 4, -97, -61, 70, 28, -29, 85, 110, 0, -107, -8, -12, -93, -121, 99, -5, 23, 60, 46, -23, 92, 67, -51, 65, 95, 84, 87, 49, -35, -78, -115};
		String expectedHex = "18049fc3461ce3556e0095f8f4a38763fb173c2ee95c43cd415f545731ddb28d";
		String expectedBase64 = "GASfw0Yc41VuAJX49KOHY/sXPC7pXEPNQV9UVzHdso0=";
			
		assertArrayEquals(expectedHash, hash.getBytes());
		assertEquals(expectedHex, hash.toHex());
		assertEquals(expectedBase64, hash.toBase64());

		print(hash, "Sha256 with salt and 10 iterations of 'Hello Sha256': ");
	}

	@Test
	public void testIterationsDemo() {
		byte[] salt = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		
		//iterations computed by the framework 
		Hash shiroIteratedHash = new Sha256Hash("Hello Sha256", salt, 10);

		//iterations computed by the client code 
		Hash clientIteratedHash = new Sha256Hash("Hello Sha256", salt);
		for (int i = 1; i < 10; i++) {
			clientIteratedHash = new Sha256Hash(clientIteratedHash.getBytes());
		}
		
		//compare results
		assertByteSourcesEquals(shiroIteratedHash, clientIteratedHash);
		
		print(clientIteratedHash, "Sha256 with salt and 10 iterations of 'Hello Sha256': ");
	}
}
