package com.free.utils.base;

import java.util.regex.Pattern;



public class ValueUtil
{
	public static int intValue(Object text)
	{
		int value = 0;
//		if (text == null) text = "";
		if (text == null) return value;
		
		String temp = text.toString().trim();
		if (temp.length() == 0) return value;
		try 
		{
			temp = temp.replace("%", "");
			int index = temp.indexOf('.');
			if (index < 0) 
			{
				value = Integer.parseInt(temp);
			}
			else
			{
				value = Integer.parseInt(temp.substring(0, index));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
//				throw LangUtil.newRuntimeException(e);
			return 0;
		}
		return value;
	}
	
	
	public static byte byteValue(Object text)
	{
		byte value = 0x00;
		if (text == null) return value;
		
		String temp = text.toString().trim();
//		if (text != null && isNumber(temp))
		{
			try
			{
				return Byte.parseByte(temp);
			}
			catch (Exception e)
			{
				return value;
			}
		}
	}

	
	public static float floatValue(Object text)
	{
		float value = 0f;
		if (text == null) return value;
		
		String temp = text.toString().trim();
//		if (text != null && isNumber(temp))
		{
			try
			{
				value = Float.parseFloat(temp);
			}
			catch (Exception e)
			{
				return value;
			}
		}
		return value;
	}
	
	public static long numberValue(String value)
	{
		try
		{
			return Long.parseLong(value);
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	public static long longValue(Object text)
	{
		long value = 0;
		if (text == null) return value;
		
		String temp = text.toString().trim();
//		if (temp != null && isNumber(temp))
		{
			try
			{
				value = Long.parseLong(temp);
			}
			catch (Exception e)
			{
				return value;
			}
		}
		return value;
	}
	
	public static boolean isNumber(String str)
	{
		int dot = 0;
		if (str == null || str.trim().length() == 0) return false;
		if (str.startsWith("-")) str = str.substring(1);
		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) > '9' || str.charAt(i) < '0') 
			{
				if (str.charAt(i) != '.') return false;
				else
					dot++;
				
				if (dot > 1) return false;
			}
		}
		
		return true;
	}
	
	public static boolean isEnum(String str)
	{
		String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$";//科学计数法正则表达式
	    Pattern pattern = Pattern.compile(regx);
		String sjiachun = "0E-7";
		return pattern.matcher(sjiachun).matches();
	}
	
	public static double doubleValue(Object text)
	{
		double value = 0.0;
		if (text == null) text = "";
		String temp = text.toString().trim();
		if (temp.length() == 0) return 0;
//		if (text != null && isNumber(temp))
		{
			value = Double.parseDouble(temp);
		}
		
		return value;
	}
	
	public static boolean boolValue(Object text)
	{
		boolean value = false;
		if (text == null) text = "";
		if (text instanceof Boolean) return (Boolean) text;
		value = Boolean.parseBoolean(text.toString().trim());
		return value;
	}
	
	public static void main(String[] args)
	{
		System.out.println(floatValue("-100.00"));
	}
	
	public static boolean boolValueC(Object text)
	{
		if (boolValue(text)) return true;
		
		if (text != null && text.equals("1")) return true;
		
		return false;
	}
	
	public static Float addFloat(int index, int length, float[] tData)
	{
		Float value = 0F;
		
		for (int i = index; i < index + length; i++) value = tData[i] + value;
		
		return value;
	}
	
	
	public static Float addFloat(int index, int length, Float[] tData)
	{
		Float value = 0F;
		
		for (int i = index; i < index + length; i++) value = tData[i] + value;
		
		return value;
	}

	public static String stringValue(Object object)
	{
		if (object == null) return "";
		if (object instanceof String) return (String) object;
		return object.toString();
	}

	public static byte[] newByteArray(byte[] tData)
	{
		if (tData == null) return null;
		if (tData.length == 0) return null;
		
		byte[] tRet = new byte[tData.length];
		System.arraycopy(tData, 0, tRet, 0, tRet.length);
		return tRet;
	}


	public static char charValue(Object value)
	{
		if (value == null) return 0x0;
		if (value.toString().length() == 0) return (char) 0x0;
		
		return value.toString().charAt(0);
	}
	
	public static int copy(byte[] tData, int offset, int length, String value)
	{
		int result = offset + length;
		byte[] tBuf;
		
		if (value != null)
		{
			tBuf = String.valueOf(value).getBytes();
			System.arraycopy(tBuf, 0, tData, offset, tBuf.length > length ? length : tBuf.length);
		}
		return result;
	}

	public static int copy(byte[] tData, int offset, int length, Long value, boolean bLeft)
	{
		int result = offset + length;
		byte[] tBuf;
		
		if (value != null)
		{
			if (bLeft) tBuf = String.format("%0" + length + "d", value).getBytes();
			else
				tBuf = String.valueOf(value).getBytes();
			
			System.arraycopy(tBuf, 0, tData, offset, tBuf.length > length ? length : tBuf.length);
		}
		return result;
	}
	
}