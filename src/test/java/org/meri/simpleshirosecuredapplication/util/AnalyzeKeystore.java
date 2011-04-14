package org.meri.simpleshirosecuredapplication.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.xerces.impl.dv.util.Base64;

/**
 * Utility class. Investigates keystore file and prints all certificates along with authority names and serial numbers.  
 */
public class AnalyzeKeystore {
	
	public static void main(String[] args) {
		AnalyzeKeystore instance = new AnalyzeKeystore();
		instance.certificateOK("src/main/resources/truststore", "secret");
	}
	
	private void certificateOK(String truststore, String password) {
		FileInputStream stream = null;

		try {
			stream = new FileInputStream(truststore);
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(stream, password.toCharArray());
			
			Enumeration<String> aliases = keyStore.aliases();
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
				
				BigInteger serialNumber = certificate.getSerialNumber();
				String issuerName = certificate.getIssuerDN().getName();
				String base64Serial = Base64.encode(serialNumber.toByteArray());
				
				System.out.println(alias + ": " + base64Serial + "|XX|" + issuerName);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

}
