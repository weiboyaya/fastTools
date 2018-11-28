package com.free.utils.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import com.free.utils.file.impl.FileUtil;

public class CxCryptTool
{
	private static byte[] tKey = "1234567890ABCDEG".getBytes();
	
	public static String DEScrypt(String data)
	{
		byte[] cryptmiw = null ;
		try
		{
			cryptmiw = CxCryptToolDes.encrypt(data.getBytes(), tKey);
		}
		catch (Exception e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
		return byte2hex(cryptmiw);
	}
	
	
	public static String DESuncrypt(String cryptdata)
	{
		byte[] cryptminw = null ;
		try
		{
			cryptminw = CxCryptToolDes.decrypt(cryptdata.getBytes(), tKey);
		}
		catch (Exception e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
		return  byte2hex(cryptminw);
	}
	
	public static void main(String[] args) throws Exception
	{
		// 密钥 MAC计算采用3des算法 ，秘钥长度为16个1
//		String masterKey = "1111111111111111";
//		System.out.println("密钥:"+masterKey);
//		
//		String aa = CxCryptToolDes3.EncryptData("1111111111111111","8888880000000000");
//		System.out.println(aa);
//		// 数据
//		
//		String sss = "";
//		StringBuffer ss = new StringBuffer("");
//		for (int i = 0; i < 100000; i++)
//		{
//			ss.append("22222222222222222222222222222222222");
//		}
//		System.out.println("hjhjhjhj");
//		String text = CxCryptTool.getMac(ss.toString(), masterKey);
//		System.out.println(text);
		long l =0;
		for(int i=0;i<10000;i++)
		{
			l = l +i;
//			System.out.println(l);
		}
		
		String filepath = "D:/sourcenanj/sunlinefront/rdp.app.front/HepWeb.war.rdpnjbank2/page/dp9801In.zip";
		for(int i=0;i<1;i++)
		{
			String md5 = getMD5ByFile(filepath);
			System.out.println(md5);
			FileUtil.writeByteArrayToFile(filepath, "112123".getBytes(),false);
		}
	}
	
	
	public static String getMac(String inData, String key) throws Exception
	{
		inData = SHA1(inData) + "00000000";
		 
		int leave = inData.length() / 16;
	    String leftKey = key.substring(0, 16);
	    String rightKey = key.substring(16);
	    String data = inData;
	    String result = null;
	    String temp = data.substring(0, 16);
	    for (int i = 0; i < leave - 1; i++) {
	      int count = (i + 1) * 16;
	      result = CxCryptToolDes3.EncryptData(leftKey, temp);
	      temp = XoResult(result, data.substring(count, count + 16));
	    }
	    result = CxCryptToolDes3.EncryptData(leftKey, CxCryptToolDes3.DecryptData(rightKey, 
	      CxCryptToolDes3.EncryptData(leftKey, temp)));

	    return result;
	}
	
	 public static String SHA1(String inStr) {
		    MessageDigest md = null;
		    String outStr = null;
		    try {
		      md = MessageDigest.getInstance("SHA-1");
		      byte[] digest = md.digest(inStr.getBytes());
		      outStr = bcdhex_to_aschex(digest);
		    }
		    catch (NoSuchAlgorithmException nsae) {
		      nsae.printStackTrace();
		    }
		    return outStr.toUpperCase();
		  }
	 
	 
	 public static String bcdhex_to_aschex(byte[] bcdhex) {
		    byte[] aschex = new byte[2];
		    String res = "";
		    String tmp = "";
		    for (int i = 0; i < bcdhex.length; i++) {
		      aschex[1] = hexLowToAsc(bcdhex[i]);
		      aschex[0] = hexHighToAsc(bcdhex[i]);
		      tmp = new String(aschex);
		      res = res + tmp;
		    }
		    return res;
		  }
	 
	 public static byte hexLowToAsc(byte xxc)
	  {
	    xxc = (byte)(xxc & 0xF);
	    if (xxc < 10)
	      xxc = (byte)(xxc + 48);
	    else
	      xxc = (byte)(xxc + 55);
	    return xxc;
	  }
	  public static byte hexHighToAsc(int xxc) {
	    xxc &= 240;
	    xxc >>= 4;
	    if (xxc < 10)
	      xxc += 48;
	    else
	      xxc += 55;
	    return (byte)xxc;
	  }

	 
	public static String XoResult(String Fstr, String Tstr)
	  {
	    byte[] from = hex2byte(Fstr.getBytes());
	    byte[] to = hex2byte(Tstr.getBytes());
	    byte[] ResaultXor = new byte[8];
	    for (int i = 0; i < 8; i++) {
	      int tmp186_184 = i;

	      ResaultXor[tmp186_184] = ((byte)(from[tmp186_184] ^ to[i]));
	    }
//	    return ResaultXor;
	    return bin2hex(ResaultXor);
	  }
	
	public static byte[] hex2byte(byte[] b)
	{
		if ((b.length % 2) != 0)
		{
			throw new IllegalArgumentException("长度不是偶数");
		}
		
		byte[] b2 = new byte[b.length / 2];
		
		for (int n = 0; n < b.length; n += 2)
		{
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}

		b = null;
		
		return b2;
	}
	
	
	public static String bin2hex(byte[] bs)
	{
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
//		byte[] bs = bin.getBytes();
		int bit;
		
		for (int i = 0; i < bs.length; i++)
		{
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}

		return sb.toString();
	}
	
	/** 
	 * java字节码转字符串 
	 * @param b 
	 * @return 
	 */
	public static String byte2hex(byte[] b) { //一个字节的数，

		// 转成16进制字符串

		String hs = "";
		String tmp = "";
		for (int n = 0; n < b.length; n++) {
			//整数转成十六进制表示

			tmp = (Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				hs = hs + "0" + tmp;
			} else {
				hs = hs + tmp;
			}
		}
		tmp = null;
		return hs.toUpperCase(); //转成大写

	}
	
	public static String pinPadding(String str)
	{  
		// 0 代表前面补充0      
		// 2代表长度为4      
		// d 代表参数为正数型      
		//左补两位数，PIN长度
		str=String.format("%02d", str.length())+str;
		int j=16;
		
		if(j<str.length()) return str;
		//右补F
		for(int i=0;str.length()<16;i++) str+="F"; 
		return str; 
	}
	
	public static String transferAccount(String pinzeed, String password)
	{
		byte[] tRandom = "0000000000000000".getBytes();
		int j = 16;
		for (int i = pinzeed.length() - 2; i >= 0 ; i--)
		{
			--j;
			if (j < 4 ) break;
			tRandom[j] = (byte) pinzeed.charAt(i);
		}
		
		tRandom = hex2byte(tRandom);
		byte[] tPswd = hex2byte(password.getBytes());
		byte[] tVal = new byte[tRandom.length];
		for (int i = 0; i < tRandom.length; i ++) tVal[i] = (byte)(tPswd[i] ^ tRandom[i]);	// 异或
		return byte2hex(tVal);
	}
	
	public static String getMD5ByFile(String filePath)
	{
		if (!FileUtil.exist(filePath))
		{
//			throw LangUtil.newRuntimeException("计算MD5失败文件[%s]不存在", filePath);
			throw new RuntimeException("计算MD5失败文件"+filePath+"不存在");
		}
		String value = null;
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(FileUtil.readFileToByteArray(filePath));
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}
	
	public static String getMD5ByData(byte[] data)
	{
		if (data == null)
		{
//			throw LangUtil.newRuntimeException("计算MD5失败,数据不能为空");
			throw new RuntimeException("计算MD5失败,数据不能为空");
		}
		String value = null;
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}
}
