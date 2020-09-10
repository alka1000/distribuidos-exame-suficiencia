package br.edu.utfpr.alunos.amir.bolsa.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 * c�digo retirado e adaptado do site: https://tassioauad.com/2015/04/08/java-criptografia-assimetrica-com-rsa/
 * acesso em 08/08/2020
 * 
 */
public class EncriptaDecriptaRSA {

	/**
	 * crypt = new JSEncrypt();
     * crypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD0UkjWJaAQ+yvZNRZch2hFuFMtRj7L6uJ/8Aua3m/UAcxKP0q1MybR0tiPggxQJiCZyTQ5LTPtbzgoziPaCc4qCMtkYmEHkYCaCrFn8tJh/EcpUc5RTtSyW2qz8oTg52A6cH/2cV+UMUmX9UBy4Y02kFTsF4sMA4nfBzUK2Ry9KQIDAQAB");
     * crypt.encrypt("PETR4;25.02;2;10-10-2020 23:59");
	 */
	private static String stringPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD0UkjWJaAQ+yvZNRZch2hFuFMtRj7L6uJ/8Aua3m/UAcxKP0q1MybR0tiPggxQJiCZyTQ5LTPtbzgoziPaCc4qCMtkYmEHkYCaCrFn8tJh/EcpUc5RTtSyW2qz8oTg52A6cH/2cV+UMUmX9UBy4Y02kFTsF4sMA4nfBzUK2Ry9KQIDAQAB";
	private static String stringPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPRSSNYloBD7K9k1FlyHaEW4Uy1GPsvq4n/wC5reb9QBzEo/SrUzJtHS2I+CDFAmIJnJNDktM+1vOCjOI9oJzioIy2RiYQeRgJoKsWfy0mH8RylRzlFO1LJbarPyhODnYDpwf/ZxX5QxSZf1QHLhjTaQVOwXiwwDid8HNQrZHL0pAgMBAAECgYAI5R4FveTv+VUFWWtw/vUK5MUSduY+YlTQt5qUjtifbVlUsq0zn6MgLH756pAsMtcpAixGkKC9Wjk/MxIXFBhPWUCdVrJHLcea1h5X+r6bOfGu3WFzq+4C1KoVJLbpBhotzrvzGoNn4UdcMhaLDC+n8vEnJISXm4CIQT9bJmLI6QJBAPvqB5pqYq+14DvZt1SSHGdt+j25hRzK1pA9tFv/MRhn7EDdSs/6xo3LRpA5RY5wMA3rB67aZfYyohgjdtp4QV8CQQD4SLqg8zleYbpETv0buHFMOJZk8iL0RaVuYIT5cK98OcN3VTj+CjTH1v97eu5fe9uHOxqFCzCp+Q+uF1RKZeZ3AkEAvYlGsTLMD4IbNUzIuBO61UMU3a4g7hnbN+mFMxadPMysH92f9T2seHDyJX9ByzqznTUxsahWwNc1yE2elh+WpwJAGJJqFf+8T8DwRE5k8ldgryByke780pwJ8VCQGHe0hmDZHXhLuaic7KI0iq3x5XVGlq2cxEoFOO0zRhF9ABfxpQJANhfrjy5wfLoyPkTbqiDH+W+P0WHA5QmwkG3rLSBjRcd1tC3qFCvHXhuDQDlZDOTS9FA69QTDhTco9qpVEChJhQ==";
	
	private static PublicKey publicKey = null;
    private static PrivateKey privateKey = null;

    private static void genKeyPair() {
    	try {
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(stringPublicKey)));
			privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(stringPrivateKey)));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    public static PublicKey getPublicKey() {
    	if (publicKey == null) {
			genKeyPair();
    	}
		return publicKey;
	}
 
    public static String getStringPublicKey() {
    	if (publicKey == null) {
			genKeyPair();
    	}
    	String string = new String (Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    	return string;
	}

	public static PrivateKey getPrivateKey() {
		if (publicKey == null) {
			genKeyPair();
    	}
		return privateKey;
	}

	public static String encrypt(String string) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
		if (publicKey == null) {
			genKeyPair();
    	}
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        String res = new String(Base64.getEncoder().encodeToString( cipher.doFinal(string.getBytes()) ));
        return res;
    }

    public static String decrypt(String string) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
    	if (publicKey == null) {
			genKeyPair();
    	}
    	Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(string.trim().getBytes())));
    }
}