package com.free.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


import com.free.utils.base.StringUtil;

/**
 * <p>Title: DES加密、解密</p>
 * <p>Description: DES加密、解密</p>
 * <p>Copyright: Copyright (c) 2004 Sunline</p>
 * <p>Company: Sunline Technologies.</p>
 * @author qiumh
 * @version 1.0
 */
public class CxCryptToolDes
{
	 /**
     * 定义加密算法
     */
    private static String Algorithm = "DES" ; //定义 加密算法,可用 DES,DESede,Blowfish
    
    /**
     * 加密
     * @param input byte[] 需加密字节码
     * @param key byte[] 密钥   DES加密 密钥必须是8的整数倍
     * @throws Exception
     * @return byte[] 加密后字节码
     */
    public static byte[] encrypt(byte[] input, byte[] key) throws Exception
    {
//        SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm) ;
        //if (ConfigDocument.isDebug)
        //{
        //    log.logInfo("加密前的二进串:" + StringUtil.toHex(input)) ;
        //    log.logInfo("加密前的字符串:" + new String(input)) ;
        //}
        DESKeySpec deskey = new DESKeySpec(key);//增加加密工厂  可以满足密钥是8的整数倍  add by hey 2015 汕头
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);
        SecretKey securetkey = keyFactory.generateSecret(deskey);
        Cipher c1 = Cipher.getInstance(Algorithm) ;
        c1.init(Cipher.ENCRYPT_MODE, securetkey) ;
        byte[] cipherByte = c1.doFinal(input) ;
        //if (ConfigDocument.isDebug)
        //    log.logInfo("加密后的二进串:" + StringUtil.toHex(cipherByte)) ;
        return cipherByte ;
    }

    /**
     * 解密
     * @param input byte[] 需解密字节码
     * @param key byte[] 密钥
     * @throws Exception
     * @return byte[] 解密后字节码
     */
    public static byte[] decrypt(byte[] input, byte[] key)
        throws Exception
    {
//        SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm) ;
        //if (ConfigDocument.isDebug)
        //    log.logInfo("解密前的信息:" + StringUtil.toHex(input)) ;
        DESKeySpec deskey = new DESKeySpec(key);//增加加密工厂  可以满足密钥是8的整数倍  add by hey 2015 汕头
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);
        SecretKey securetkey = keyFactory.generateSecret(deskey);
        
        Cipher c1 = Cipher.getInstance(Algorithm) ;
        c1.init(Cipher.DECRYPT_MODE, securetkey) ;

        byte[] clearByte = c1.doFinal(input) ;
        //if (ConfigDocument.isDebug)
        //{
        //    log.logInfo("解密后的二进串:" + StringUtil.toHex(clearByte)) ;
        //    log.logInfo("解密后的字符串:" + (new String(clearByte))) ;
        //}
        return clearByte ;
    }
    
	public static String decryptByDes(String key,String text)
	{
		if(key == null || key.trim().length() == 0) return text;
		if(text == null || text.trim().length()==0)return text;
		byte[]texts = text.getBytes();
		byte[]keys = key.getBytes();
		try 
		{
			text = new String(decrypt( StringUtil.hex2byte(texts), keys));
		} catch (Exception e) 
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
		return text;
	}
	public static String encryptByDes(String key,String text)
	{
		if(key == null || key.trim().length() == 0) return text;
		if(text == null || text.trim().length()==0)return text;
		byte[] texts = text.getBytes();
		byte[]keys = key.getBytes();
		try 
		{
			text = new String(StringUtil.byte2hex(encrypt( texts, keys)));
		}
		catch (Exception e) 
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
		return text;
	}
    
    public static void main(String[] args) throws Exception
    {
    	String key = "1234567890ABCDEF";
//    				  1234567890ABCDEF 1234567890ABCDEF 1234567890ABCDEF	
//    				  96D0028878D58C89E312EFEC9F815557FEB959B7D4642FCB
		String password = "1234567890ABCDEF";
		
		System.out.println("pass:"+password);
		password = encryptByDes(key, password);
		System.out.println("pass:"+password);
		password = decryptByDes(key, password);
		System.out.println("pass:"+password);
    	
    }
}
