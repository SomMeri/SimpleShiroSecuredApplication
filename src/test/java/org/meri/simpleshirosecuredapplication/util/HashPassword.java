package org.meri.simpleshirosecuredapplication.util;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.SimpleByteSource;

public class HashPassword {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		simpleHash("heslo");
		simpleSaltedHash("administrator", "heslo");
		simpleSaltedHash("friendlyrepairman", "heslo");
		simpleSaltedHash("unfriendlyrepairman", "heslo");
		simpleSaltedHash("mathematician", "heslo");
		simpleSaltedHash("physicien", "heslo");
		simpleSaltedHash("productsales", "heslo");
		simpleSaltedHash("servicessales", "heslo");

	}

	private static String simpleHash(String password) {
	  Sha256Hash sha256Hash = new Sha256Hash(password);
		String result = sha256Hash.toHex();

		System.out.println("Simple hash: " + result);
	  return result;
  }

	private static String simpleSaltedHash(String username, String password) {
	  Sha256Hash sha256Hash = new Sha256Hash(password, (new SimpleByteSource("random_salt_value_" + username)).getBytes());
		String result = sha256Hash.toHex();

		System.out.println(username + " simple salted hash: " + result);
	  return result;
  }

}
