package org.meri.shiro.cryptographydemo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

public class EncodingTestCase extends CryptoTestCase {

	@Test
	public void textToByteArray() {
		String encodeMe = "Hello, I'm a text.";
		byte[] bytes = CodecSupport.toBytes(encodeMe);
		
		String decoded = CodecSupport.toString(bytes);
		assertEquals(encodeMe, decoded);
	}

	@Test
	public void testByteSourceHexadecimal() {
		byte[] encodeMe = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20};
		
		ByteSource byteSource = ByteSource.Util.bytes(encodeMe);
		String hexadecimal = byteSource.toHex();
		assertEquals("020406080a0c0e101214", hexadecimal);
		
		byte[] decoded = Hex.decode(hexadecimal);
		assertArrayEquals(encodeMe, decoded);
	}

	@Test
	public void testByteSourceBase64() {
		byte[] encodeMe = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20};
		
		ByteSource byteSource = ByteSource.Util.bytes(encodeMe);
		String base64 = byteSource.toBase64();
		assertEquals("AgQGCAoMDhASFA==", base64);
		
		byte[] decoded = Base64.decode(base64);
		assertArrayEquals(encodeMe, decoded);
	}

	@Test
	public void testStaticHexadecimal() {
		byte[] encodeMe = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20};
		
		String hexadecimal = Hex.encodeToString(encodeMe);
		assertEquals("020406080a0c0e101214", hexadecimal);
		
		byte[] decoded = Hex.decode(hexadecimal);
		assertArrayEquals(encodeMe, decoded);
	}


	@Test
	public void testStaticBase64() {
		byte[] encodeMe = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20};
		
		String base64 = Base64.encodeToString(encodeMe);
		assertEquals("AgQGCAoMDhASFA==", base64);
		
		byte[] decoded = Base64.decode(base64);
		assertArrayEquals(encodeMe, decoded);
	}

}
