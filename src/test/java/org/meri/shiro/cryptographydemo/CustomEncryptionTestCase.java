package org.meri.shiro.cryptographydemo;

import static org.junit.Assert.*;

import java.security.Key;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.BlowfishCipherService;
import org.apache.shiro.crypto.CryptoException;
import org.apache.shiro.crypto.DefaultBlockCipherService;
import org.apache.shiro.crypto.OperationMode;
import org.apache.shiro.crypto.PaddingScheme;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

public class CustomEncryptionTestCase extends CryptoTestCase {

	@Test(expected=CryptoException.class)
	public void aesWrongBlockSize() {
		String secret = "Tell nobody!";
		AesCipherService cipher = new AesCipherService();
		// set wrong block size 
		cipher.setBlockSize(200);

		// generate key with default 128 bits size
		Key key = cipher.generateNewKey();
		byte[] keyBytes = key.getEncoded();

		// encrypt the secret
		byte[] secretBytes = CodecSupport.toBytes(secret);
		cipher.encrypt(secretBytes, keyBytes);
	}

	@Test(expected=CryptoException.class)
	public void aesWrongPadding() {
		String secret = "Tell nobody!";
		BlowfishCipherService cipher = new BlowfishCipherService();
		// set wrong block size 
		cipher.setPaddingScheme(PaddingScheme.PKCS1);

		// generate key with default 128 bits size
		Key key = cipher.generateNewKey();
		byte[] keyBytes = key.getEncoded();

		// encrypt the secret
		byte[] secretBytes = CodecSupport.toBytes(secret);
		cipher.encrypt(secretBytes, keyBytes);
	}

	@Test
	public void opensslDes3Decryption() {
		String hexInitializationVector = "F758CEEB7CA7E188";
		String base64Ciphertext = "GmfvxhbYJbVFT8Ad1Xc+Gh38OBmhzXOV";

		//decode secret message and initialization vector
		byte[] iniVector = Hex.decode(hexInitializationVector);
		byte[] ciphertext = Base64.decode(base64Ciphertext);

		//combine initialization vector and ciphertext together
		byte[] ivCiphertext = combine(iniVector, ciphertext);

		//initialization vector must be 8 bytes long
		assertEquals(8, iniVector.length);
		
		//decode secret key
		String hexSecretKey = "B9FAB84B65870109A6E8707BC95151C245BF18204C028A6A";
		byte[] keyBytes = Hex.decode(hexSecretKey);

        //initialize cipher and decrypt the message
		OpensslDes3CipherService cipher = new OpensslDes3CipherService();
		ByteSource decrypted = cipher.decrypt(ivCiphertext, keyBytes);
		
		//verify result
		String theMessage = CodecSupport.toString(decrypted.getBytes());
		assertEquals("yeahh, that worked!\n", theMessage);
	}

	private byte[] combine(byte[] iniVector, byte[] ciphertext) {
		byte[] ivCiphertext = new byte[iniVector.length + ciphertext.length];
        System.arraycopy(iniVector, 0, ivCiphertext, 0, iniVector.length);
        System.arraycopy(ciphertext, 0, ivCiphertext, iniVector.length, ciphertext.length);
		return ivCiphertext;
	}

	public class OpensslDes3CipherService extends DefaultBlockCipherService {

		public OpensslDes3CipherService() {
			super("DESede");
	        setMode(OperationMode.CBC);
	        setPaddingScheme(PaddingScheme.PKCS5);
	        setInitializationVectorSize(64);
		}
		
	}

	
}
