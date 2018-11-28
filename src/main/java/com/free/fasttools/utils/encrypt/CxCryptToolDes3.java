package com.free.utils.encrypt;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * <p>Title: DES加密、解密</p>
 * <p>Description: DES加密、解密</p>
 * <p>Copyright: Copyright (c) 2004 Sunline</p>
 * <p>Company: Sunline Technologies.</p>
 * @author qiumh
 * @version 1.0
 */
public class CxCryptToolDes3
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
    
    static String DES = "DES/ECB/NoPadding";
    static String TriDes = "DESede/ECB/NoPadding";

    public static byte[] hex2byte(String str)
    {
      int len = str.length();
      String stmp = null;
      byte[] bt = new byte[len / 2];
      for (int n = 0; n < len / 2; n++) {
        stmp = str.substring(n * 2, n * 2 + 2);
        bt[n] = ((byte)Integer.parseInt(stmp, 16));
      }
      return bt;
    }

    public static String byte2hex(byte[] b)
    {
      String hs = "";
      String stmp = "";
      for (int n = 0; n < b.length; n++) {
        stmp = Integer.toHexString(b[n] & 0xFF);
        if (stmp.length() == 1)
          hs = hs + "0" + stmp;
        else {
          hs = hs + stmp;
        }
        /*if (n < b.length - 1) {
          hs = hs;
        }*/
      }
      return hs.toUpperCase();
    }

    public static byte[] DesEncrypt(byte[] key, byte[] data)
    {
      try
      {
        KeySpec ks = new DESKeySpec(key);
        SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
        SecretKey ky = kf.generateSecret(ks);

        Cipher c = Cipher.getInstance(DES);
        c.init(1, ky);
        return c.doFinal(data);
      } catch (Exception e) {
        e.printStackTrace();
      }return null;
    }

    public static byte[] DesDecrypt(byte[] key, byte[] data)
    {
      try
      {
        KeySpec ks = new DESKeySpec(key);
        SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
        SecretKey ky = kf.generateSecret(ks);

        Cipher c = Cipher.getInstance(DES);
        c.init(2, ky);
        return c.doFinal(data);
      } catch (Exception e) {
        e.printStackTrace();
      }return null;
    }

    public static byte[] TDesEncrypt(byte[] key, byte[] data)
    {
      try
      {
        byte[] k = new byte[24];

        int len = data.length;
        if (data.length % 8 != 0) {
          len = data.length - data.length % 8 + 8;
        }
        byte[] needData = (byte[])null;
        if (len != 0) {
          needData = new byte[len];
        }
        for (int i = 0; i < len; i++) {
          needData[i] = 0;
        }

        System.arraycopy(data, 0, needData, 0, data.length);

        if (key.length == 16) {
          System.arraycopy(key, 0, k, 0, key.length);
          System.arraycopy(key, 0, k, 16, 8);
        } else {
          System.arraycopy(key, 0, k, 0, 24);
        }

        KeySpec ks = new DESedeKeySpec(k);
        SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
        SecretKey ky = kf.generateSecret(ks);

        Cipher c = Cipher.getInstance(TriDes);
        c.init(1, ky);
        return c.doFinal(needData);
      } catch (Exception e) {
        e.printStackTrace();
      }return null;
    }

    public static byte[] TDesDecrypt(byte[] key, byte[] data)
    {
      try
      {
        byte[] k = new byte[24];

        int len = data.length;
        if (data.length % 8 != 0) {
          len = data.length - data.length % 8 + 8;
        }
        byte[] needData = (byte[])null;
        if (len != 0) {
          needData = new byte[len];
        }
        for (int i = 0; i < len; i++) {
          needData[i] = 0;
        }

        System.arraycopy(data, 0, needData, 0, data.length);

        if (key.length == 16) {
          System.arraycopy(key, 0, k, 0, key.length);
          System.arraycopy(key, 0, k, 16, 8);
        } else {
          System.arraycopy(key, 0, k, 0, 24);
        }
        KeySpec ks = new DESedeKeySpec(k);
        SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
        SecretKey ky = kf.generateSecret(ks);

        Cipher c = Cipher.getInstance(TriDes);
        c.init(2, ky);
        return c.doFinal(needData);
      } catch (Exception e) {
        e.printStackTrace();
      }return null;
    }

    public static String DecryptData(String key, String data)
    {
      if ((key.length() != 16) && (key.length() != 32) && (key.length() != 48))
      {
        return null;
      }
      if (data.length() % 16 != 0)
      {
        return "";
      }
      int lenOfKey = 0;
      lenOfKey = key.length();
      String strEncrypt = "";
      byte[] sourData = hex2byte(data);
      switch (lenOfKey)
      {
      case 16:
        byte[] deskey8 = hex2byte(key);
        byte[] encrypt = DesDecrypt(deskey8, sourData);
        strEncrypt = byte2hex(encrypt);
        break;
      case 32:
      case 48:
        String newkey1 = "";
        if (lenOfKey == 32)
        {
          String newkey = key.substring(0, 16);
          newkey1 = key + newkey;
        }
        else {
          newkey1 = key;
        }
        byte[] deskey24 = hex2byte(newkey1);
        byte[] desEncrypt = TDesDecrypt(deskey24, sourData);
        strEncrypt = byte2hex(desEncrypt);
      }
      return strEncrypt;
    }

    public static String EncryptData(String key, String data)
    {
      if ((key.length() != 16) && (key.length() != 32) && (key.length() != 48))
      {
        return null;
      }
      if (data.length() % 16 != 0)
      {
        return "数据长度不是8的倍数！~";
      }
      int lenOfKey = key.length();
      String strEncrypt = "";
      byte[] sourData = hex2byte(data);
      switch (lenOfKey)
      {
      case 16:
        byte[] deskey8 = hex2byte(key);
        byte[] encrypt = DesEncrypt(deskey8, sourData);
        strEncrypt = byte2hex(encrypt);
        break;
      case 32:
      case 48:
        String newkey1 = "";
        if (lenOfKey == 32)
        {
          String newkey = key.substring(0, 16);
          newkey1 = key + newkey;
        }
        else {
          newkey1 = key;
        }
        byte[] deskey24 = hex2byte(newkey1);
        byte[] desEncrypt = TDesEncrypt(deskey24, sourData);
        strEncrypt = byte2hex(desEncrypt);
      }

      return strEncrypt;
    }

    public static void main(String[] args) throws Exception
    {
    	String pwd="0812345678FFFFFF";
    	System.out.println("pwd="+pwd);
    	
        String key = "1234567890ABCDEF";
        
       String jiambyte = EncryptData(pwd, key);
        System.out.println("jiampasswd="+jiambyte + "," + jiambyte);
        
       /* jiambyte = DecryptData(jiambyte, new String(key));
        
        System.out.println("jiempasswd="+new String(jiambyte));*/
    }
}
