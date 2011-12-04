package org.meri.shiro.cryptographydemo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.BlowfishCipherService;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

public class EncryptionTestCase extends CryptoTestCase {

	@Test
	public void encryptStringMessage() {
		String secret = "Tell nobody!";
		AesCipherService cipher = new AesCipherService();

		// generate key with default 128 bits size
		Key key = cipher.generateNewKey();
		byte[] keyBytes = key.getEncoded();

		// encrypt the secretsetGenerateInitializationVectors
		byte[] secretBytes = CodecSupport.toBytes(secret);
		ByteSource encrypted = cipher.encrypt(secretBytes, keyBytes);

		// decrypt the secret
		byte[] encryptedBytes = encrypted.getBytes();
		ByteSource decrypted = cipher.decrypt(encryptedBytes, keyBytes);
		String secret2 = CodecSupport.toString(decrypted.getBytes());

		// verify correctness
		assertEquals(secret, secret2);
	}

	@Test
	public void encryptStream() {
		InputStream secret = openSecretInputStream();
		BlowfishCipherService cipher = new BlowfishCipherService();

		// generate key with default 128 bits size
		Key key = cipher.generateNewKey();
		byte[] keyBytes = key.getEncoded();

		// encrypt the secret
		OutputStream encrypted = openSecretOutputStream();
		try {
			cipher.encrypt(secret, encrypted, keyBytes);
		} finally {
			// The cipher does not flush neither close streams.
			closeStreams(secret, encrypted);
		}

		// decrypt the secret
		InputStream encryptedInput = convertToInputStream(encrypted);
		OutputStream decrypted = openSecretOutputStream();
		try {
			cipher.decrypt(encryptedInput, decrypted, keyBytes);
		} finally {
			// The cipher does not flush neither close streams.
			closeStreams(secret, encrypted);
		}

		// verify correctness
		assertStreamsEquals(secret, decrypted);
	}

	@Test
	public void unpredictableEncryptionProof() {
		String secret = "Tell nobody!";
		AesCipherService cipher = new AesCipherService();

		// generate key with default 128 bits size
		Key key = cipher.generateNewKey();
		byte[] keyBytes = key.getEncoded();

		// encrypt two times
		byte[] secretBytes = CodecSupport.toBytes(secret);
		ByteSource encrypted1 = cipher.encrypt(secretBytes, keyBytes);
		ByteSource encrypted2 = cipher.encrypt(secretBytes, keyBytes);

		// verify correctness
		assertArrayNotSame(encrypted1.getBytes(), encrypted2.getBytes());
	}

	@Test
	public void unpredictableEncryptionNoIVProof() {
		String secret = "Tell nobody!";
		AesCipherService cipher = new AesCipherService();
		cipher.setGenerateInitializationVectors(false);

		// generate key with default 128 bits size
		Key key = cipher.generateNewKey();
		byte[] keyBytes = key.getEncoded();

		// encrypt two times
		byte[] secretBytes = CodecSupport.toBytes(secret);
		ByteSource encrypted1 = cipher.encrypt(secretBytes, keyBytes);
		ByteSource encrypted2 = cipher.encrypt(secretBytes, keyBytes);

		// verify correctness
		assertArrayNotSame(encrypted1.getBytes(), encrypted2.getBytes());
	}

	@Test
	public void predictableEncryption() {
		String secret = "Tell nobody!";
		AesCipherService cipher = new AesCipherService();
		cipher.setSecureRandom(new ConstantSecureRandom());
		cipher.setGenerateInitializationVectors(false);

		// define the key
		byte[] keyBytes = {5, -112, 36, 113, 80, -3, -114, 77, 38, 127, -1, -75, 65, -102, -13, -47};

		// encrypt first time
		byte[] secretBytes = CodecSupport.toBytes(secret);
		ByteSource encrypted = cipher.encrypt(secretBytes, keyBytes);

		// verify correctness, the result is always the same
		byte[] expectedBytes = {76, 69, -49, -110, -121, 97, -125, -111, -11, -61, 61, 11, -40, 26, -68, -58};
		assertArrayEquals(expectedBytes, encrypted.getBytes());
	}

	@Test(expected=RuntimeException.class)
	public void aesWrongKeySize() {
		AesCipherService cipher = new AesCipherService();
		
		//The call throws an exception. Aes supports only keys of 128, 192, and 256 bits.
		cipher.generateNewKey(200);
	}

	@Test
	public void aesGoodKeySize() {
		AesCipherService cipher = new AesCipherService();
		//aes supports only keys of 128, 192, and 256 bits
		cipher.generateNewKey(128);
		cipher.generateNewKey(192);
		cipher.generateNewKey(256);
	}

	private InputStream convertToInputStream(OutputStream stream) {
		ByteArrayOutputStream ba = (ByteArrayOutputStream) stream;
		return new ByteArrayInputStream(ba.toByteArray());
	}

	private void closeStreams(InputStream secret, OutputStream encrypted) {
		if (secret != null) {
			try {
				secret.close();
			} catch (IOException ex) {
				// this is just a test case, real handling is not necessary
				throw new RuntimeException(ex);
			}
		}
		if (encrypted != null) {
			try {
				encrypted.close();
			} catch (IOException ex) {
				// this is just a test case, real handling is not necessary
				throw new RuntimeException(ex);
			}
		}
	}

	private ByteArrayInputStream openSecretInputStream() {
		byte[] secretContent = CodecSupport.toBytes("Tell nobody!");
		return new ByteArrayInputStream(secretContent);
	}

	private ByteArrayOutputStream openSecretOutputStream() {
		return new ByteArrayOutputStream();
	}

}

@SuppressWarnings("serial")
class ConstantSecureRandom extends SecureRandom {

	@Override
	public synchronized void nextBytes(byte[] bytes) {
		Arrays.fill(bytes, (byte) 0);
	}

	@Override
	public boolean nextBoolean() {
		return true;
	}

	@Override
	public double nextDouble() {
		return 0;
	}

	@Override
	public float nextFloat() {
		return 0;
	}

	@Override
	public synchronized double nextGaussian() {
		return 0;
	}

	@Override
	public int nextInt() {
		return 0;
	}

	@Override
	public int nextInt(int arg0) {
		return 0;
	}

	@Override
	public long nextLong() {
		return 0;
	}
	
}
