package org.meri.shiro.cryptographydemo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.shiro.util.ByteSource;

public class CryptoTestCase {

	private static final String BYTES_LOG_DELIMITER = " ";

	public static void assertByteSourcesNotSame(ByteSource first, ByteSource second) {
		assertArrayNotSame(first.getBytes(), second.getBytes());
		assertNotSame(first.toHex(), second.toHex());
		assertNotSame(first.toBase64(), second.toBase64());
	}

	public static void assertByteSourcesEquals(ByteSource first, ByteSource second) {
		assertArrayEquals(first.getBytes(), second.getBytes());
		assertEquals(first.toHex(), second.toHex());
		assertEquals(first.toBase64(), second.toBase64());
	}
	
	public static void assertArrayNotSame(byte[] bytes, byte[] bytes2) {
		if (bytes.length != bytes2.length)
			return ;
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i]!=bytes2[i])
				return ;
		}
		
		fail("Arrays are supposed to be different.");
	}

	public static void assertStreamsEquals(InputStream secret, OutputStream decrypted) {
		ByteArrayOutputStream output = (ByteArrayOutputStream) decrypted;
		byte[] ob = output.toByteArray();
		byte[] ib = new byte[ob.length];
		int read = 0;
	
		try {
			secret.reset();
		    read = secret.read(ib);
		} catch (IOException ex) {
			// this is just a test case, real handling is not necessary
			throw new RuntimeException(ex);
		}
		
		assertEquals(ob.length, read);
		assertArrayEquals(ib, ob);
	}

	protected void print(ByteSource bs) {
		print(bs, null);
	}

	protected void print(ByteSource bs, String name) {
		byte[] bytes = bs.getBytes();
		print(bytes, name);
		
		String offset = offset(name);
		
		System.out.println(offset + "Hexadecimal: " + bs.toHex());
		System.out.println(offset + "Base64: " + bs.toBase64());

		System.out.println();
	}

	public void print(byte[] bytes) {
		print(bytes, null);
	}
	
	public void print(byte[] bytes, String name) {
		String offset = offset(name);
		
		System.out.print(offset + "Bytes: ");
		for (int i=0; i<bytes.length; i++)
			System.out.print(bytes[i] + BYTES_LOG_DELIMITER);

		System.out.println();
	}

	private String offset(String name) {
		String offset = "";
		if (name!=null) {
			System.out.println(name);
			offset = "  ";
		}
		return offset;
	}

	
}
