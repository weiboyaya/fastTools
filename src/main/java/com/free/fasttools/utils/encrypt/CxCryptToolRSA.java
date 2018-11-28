package com.free.utils.encrypt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class CxCryptToolRSA 
{
	private static String RSA_Algorithm="RSA";
	
	/**
	 * @param input byte[] 密码
	 * @param key byte[] 密钥
	 * @return byte[] 加密后
	 */
	public static byte[] encrypt(byte[] input, byte[] key) throws Exception
	{
		KeyFactory  keyFactory = KeyFactory.getInstance(RSA_Algorithm);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(RSA_Algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptminw = cipher.doFinal(input);
		return encryptminw;
	}
	
	public static void main(String[] args) 
	{
		String pwd="222221";
    	
        byte[] key = "1234567890ABCDEF".getBytes();
        try {
			System.out.println(encrypt(pwd.getBytes(), key).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
