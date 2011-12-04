package org.meri.shiro.cryptographydemo;

import static org.junit.Assert.*;

import org.apache.shiro.crypto.hash.DefaultHasher;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.HashResponse;
import org.apache.shiro.crypto.hash.Hasher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHashRequest;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

public class HasherTestCase extends CryptoTestCase {

	@Test
	public void hasherDemo() {
		ByteSource originalPassword = ByteSource.Util.bytes("Super Secret Password");
		ByteSource suppliedPassword = originalPassword;
		Hasher hasher = new DefaultHasher();
		
		//use hasher to compute password hash
		HashRequest originalRequest = new SimpleHashRequest(originalPassword);
		HashResponse originalResponse = hasher.computeHash(originalRequest);
		
		//Use salt from originalResponse to compare stored password with user supplied password. We assume that user supplied correct password.
		HashRequest suppliedRequest = new SimpleHashRequest(suppliedPassword, originalResponse.getSalt());
		HashResponse suppliedResponse = hasher.computeHash(suppliedRequest);
		
		assertEquals(originalResponse.getHash(), suppliedResponse.getHash());
		
		//important: the same request hashed twice may lead to different results 
		HashResponse anotherResponse = hasher.computeHash(originalRequest);
		assertNotSame(originalResponse.getHash(), anotherResponse.getHash());
	}

	@Test
	public void fullyConfiguredHasher() {
		ByteSource originalPassword = ByteSource.Util.bytes("Secret");

		byte[] baseSalt = {1, 1, 1, 2, 2, 2, 3, 3, 3};
		int iterations = 10;
		DefaultHasher hasher = new DefaultHasher();
		hasher.setBaseSalt(baseSalt);
		hasher.setHashIterations(iterations);
		hasher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
		
		//custom public salt
		byte[] publicSalt = {1, 3, 5, 7, 9};
		ByteSource salt = ByteSource.Util.bytes(publicSalt);
		
		//use hasher to compute password hash
		HashRequest request = new SimpleHashRequest(originalPassword, salt);
		HashResponse response = hasher.computeHash(request);
		
		byte[] expectedHash = {-108, 19, -40, 8, 89, -59, 115, -4, 78, 48, 110, 115, -117, 54, -80, 72, 44, 22, -100, -24, -23, -114, -24, -128, -95, -125, 2, -67, -40, 83, 90, -103};
		assertArrayEquals(expectedHash, response.getHash().getBytes());
		
		print(response.getHash());
	}

}
