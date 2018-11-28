package com.free.utils.base;

import com.free.utils.file.impl.CloseUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


public class StringUtil
{
	public static final String OS_UNIX = "UNIX";
	public static final String OS_WINX = "WINX";
	
	 /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟" };
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;
    
    public static String toChinaNumber(String numberOfMoney)
    {
		if (numberOfMoney == null || numberOfMoney.trim().length() == 0) numberOfMoney = "0"; 
    	return toChinaNumber(new BigDecimal(numberOfMoney));
    }
    public static String toChinaNumber(BigDecimal numberOfMoney)
    {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0)
        {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
   
	
	public static String getSystemLine()
	{
		return getSystemLine(OS_WINX);
	}
	public static String getSystemLine(String MACRO)
	{
		return "\r\n";
	}
	public static String bin2hex(String bin)
	{
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		byte[] bs = bin.getBytes();
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

	public static String hex2bin(String hex)
	{
		String digital = "0123456789ABCDEF";
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}
		return new String(bytes);
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
	
	
	
	public static String fillString(String source, boolean right, String fill, int length)
	{
		if (source == null) source = "";
		if (source.length() >= length) return source;
		
		char[] tFill = fill.toCharArray();
		char[] tResult = new char[length];
		if (right)
		{
			for (int i = 0;i < source.length(); i++) tResult[i] = source.charAt(i);
			for (int i = 1; i <= length - source.length(); i++)
			{
				int index = (tFill.length - (i + tFill.length) % tFill.length) % tFill.length;
				tResult[length - i] = tFill[index];
			}
		}
		else
		{
			for (int i = 0; i < source.length(); i++) tResult[length - source.length() + i] = source.charAt(i);
			for (int i = 0; i < length - source.length(); i++)
			{
				int index = (i + tFill.length) % tFill.length;
				tResult[i] = tFill[index];
			}
		}
		
		return new String(tResult);
	}

	public static String fillString(String source, int sourceIndex, int length, char cFill)
	{
		StringBuffer sbText = new StringBuffer("");
		char[] tData = source.toCharArray();
		for (int i = 0; i < tData.length; i++)
		{
			sbText.append(tData[i]);
			if (sourceIndex == i)
			{
				for (int j = 0; j < length; j++) sbText.append(cFill);
			}
		}
		return sbText.toString();
	}
	
	public static byte[] tabByteArray(int count)
	{
		byte[] tData = new byte[count];
		for (int i = 0 ;i < count; i++) tData[i] = '\t';
		return tData;
	}
	
	public static String tabString(int count)
	{
		return fillString("", 0, count, '\t');
	}
	
	public static List<String> readLineContext(String context)
	{
		String result = null;
		List<String> lines = new ArrayList<String>();
		ByteArrayInputStream bis = null;
		LineNumberReader reader = null;
		InputStreamReader isReader = null;
		
		try {
			bis = new ByteArrayInputStream(context.getBytes("UTF-8"));
			isReader = new InputStreamReader(bis,"UTF-8");
			reader = new LineNumberReader(isReader);
			while ((result = reader.readLine()) != null)
			{
				lines.add(result);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public static String readLineContext(String context, int line)
	{
		String result = null;
		
		ByteArrayInputStream bis = null;
		LineNumberReader reader = null;
		InputStreamReader isReader = null;
		
		
		try {
			bis = new ByteArrayInputStream(context.getBytes("UTF-8"));
			isReader = new InputStreamReader(bis,"UTF-8");
			reader = new LineNumberReader(isReader);
			while ((result = reader.readLine()) != null)
			{
				if (line == reader.getLineNumber()) break;
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}

	public static String unpack(String ret, String sLimit, int i)
	{
		String[] tData = ret.split(sLimit);
		if (tData.length > i) return tData[i];
		
		return "";
	}
	
	public static String getParaString(String... paras)
	{
		StringBuffer sb = new StringBuffer("");
		
		for (Object obj : paras) sb.append(obj).append(",");
		if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
	
	public static String getParaString(Object... paras)
	{
		StringBuffer sb = new StringBuffer("");
		
		for (Object obj : paras) sb.append(obj).append(",");
		if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
	
	public static String getArrayString(Object[] widths)
	{
		if (widths == null) return null;
		StringBuffer sbText = new StringBuffer("[");
		for (Object obj : widths)
		{
			sbText.append(obj).append(",");
		}
		sbText.deleteCharAt(sbText.length() - 1);
		sbText.append("]");
		
		return sbText.toString();
	}
	
	
	public static String subByteString(String context, int index, int length)
	{
		int start = 0, end = 0;
		char[] tData = context.toCharArray();
		StringBuffer sbText = new StringBuffer("");
		String tmp;
		for (char data : tData)
		{
			tmp = "" + data;
			if (index > start)
			{
				start = iByteLength(tmp) + start;
			}
			else
			{
				if (length > end)
				{
					end = end + iByteLength(tmp);
					sbText.append(data);
				}
				else
				{
					end = start + end;
					break;
				}
			}
		}
		return sbText.toString();
	}
	
	public static void main(String[] args)
	{
		String context = "张振123振";
		System.out.println(iPrintLength(context));
	}
	
	public static int iPrintLength(String context)
	{
		if (context == null) return -1;
		
		try
		{
			//张振123振
			int chineseLength = context.getBytes("GBK").length;
			return chineseLength;
		}
		catch (UnsupportedEncodingException e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	public static int iByteLength(String context)
	{
		if (context == null) return -1;
		
		try
		{
			return context.getBytes("GBK").length;
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static String trim(String text)
	{
		if (text != null)
		{
			StringBuffer sbText = new StringBuffer("");
			for (int i = 0; i < text.length(); i++)
			{
				if (text.charAt(i) != ' ') sbText.append(text.charAt(i));
			}
			
			return sbText.toString();
		}
		return text;
	}
	
	
	public static String getChineseString(String text, int width)
	{
		int tempLen = 0;
		StringBuffer sbText = new StringBuffer("");
		
		for (int i = 0; i < text.length(); i++)
		{
			sbText.append(text.charAt(i));
			tempLen = tempLen + StringUtil.iByteLength(text.charAt(i) + "");
			if (tempLen == width || tempLen + 1 == width)
			{
				if (tempLen + 1 == width && text.length() - i > 1)
				{
					if (StringUtil.iByteLength(text.charAt(i + 1) + "") == 1)
					{
						sbText.append(text.charAt(i + 1));
					}
				}
				break;
			}
		}
		
		return sbText.toString();
	}
	
	public static int getChineseLength(String text)
	{
		int length = 0;
		for (int i = 0; i < text.length(); i++)
		{
			try {
				length = length + (text.charAt(i) + "").getBytes("GBK").length;
			} catch (UnsupportedEncodingException e) {
//				throw LangUtil.newRuntimeException(e);
				throw new RuntimeException(e);
			}
		}
		return length;
	}
	
	public static List<String> splitChineseString(String text, int width)
	{
		List<String> lines = new ArrayList<String>();
		int tempLen = 0;
		StringBuffer sbText = new StringBuffer("");
		
		for (int i = 0; i < text.length(); i++)
		{
			sbText.append(text.charAt(i));
			tempLen = tempLen + StringUtil.iByteLength(text.charAt(i) + "");
			if (tempLen == width || tempLen + 1 == width)
			{
				if (tempLen + 1 == width && text.length() - i > 1)
				{
					if (StringUtil.iByteLength(text.charAt(i + 1) + "") == 1)
					{
						sbText.append(text.charAt(i + 1));
						i++;
					}
				}
				
				lines.add(sbText.toString());
				sbText.delete(0, sbText.length());
				tempLen = 0;
			}
		}
		
		if (sbText.length() > 0) lines.add(sbText.toString());
		
		return lines;
	}

	public static String reaplce(String password, char replace)
	{
		if (password == null || password.trim().length() == 0) return null;
		StringBuffer sbText = new StringBuffer("");
		for (int i = 0; i < password.length(); i++) sbText.append(replace);
		return sbText.toString();
	}
	
	
	public static final char[] tFormatString = "%s".toCharArray();
	public static String format(String message, Object... paras)
	{
		if (paras == null || paras.length == 0) return message;
		int index = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < message.length(); i++)
		{
			if (message.charAt(i) == tFormatString[0] && message.charAt(i + 1) == tFormatString[1])
			{
				if (paras.length == index) continue;
				if (paras[index] == null) sb.append("null");
				else
					sb.append(paras[index].toString());
				
				index++;
				i++;
			}
			else
				sb.append(message.charAt(i));
		}
		
		return sb.toString();
	}
	
	
	public static float[] spiltFloats(String weights)
	{
		String[] tValues = weights.split(",");
		float[] fValues = new float[tValues.length];
		for (int i = 0; i < tValues.length; i++)
		{
			fValues[i] = ValueUtil.floatValue(tValues[i]);
		}
		return fValues;
	}
	
	public static boolean[] spiltBooleans(String weights)
	{
		String[] tValues = weights.split(",");
		boolean[] fValues = new boolean[tValues.length];
		for (int i = 0; i < tValues.length; i++)
		{
			fValues[i] = ValueUtil.boolValueC(tValues[i]);
		}
		return fValues;
	}
	
	
	public static int[] spiltIntegers(String weights)
	{
		String[] tValues = weights.split(",");
		int[] fValues = new int[tValues.length];
		for (int i = 0; i < tValues.length; i++)
		{
			fValues[i] = ValueUtil.intValue(tValues[i]);
		}
		return fValues;
	}
	
	public static byte[] compressBytes(byte input[]) 
	{
		Deflater compresser = new Deflater();
		
		compresser.reset();
		compresser.setInput(input);
		compresser.setLevel(9);
		compresser.finish();
		byte output[] = new byte[0];
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try {
			byte[] buf = new byte[1024];
			int got;
			while (!compresser.finished()) {
				got = compresser.deflate(buf);
				o.write(buf, 0, got);
			}
			output = o.toByteArray();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
			}
		}
		return output;
	}
	
	
	public static byte[] decompressBytes(byte input[]) 
	{
		Inflater decompresser = new Inflater();
		byte[] output = null;
		decompresser.reset();
		decompresser.setInput(input);
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try {
			byte[] buf = new byte[1024];
			int got;
			while (!decompresser.finished()) {
				got = decompresser.inflate(buf);
				o.write(buf, 0, got);
			}
			output = o.toByteArray();
		} catch (Throwable e) {
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
			
		} finally {
			CloseUtil.close(o);
		}
		return output;
	}
    public static boolean bHaveChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    
    public static String rtrim(String s) 
	{
		int len = s.length();
		byte st = 0;

		for (char[] val = s.toCharArray(); st < len && val[len - 1] <= 32; --len)
		{
			;
		}

		return len < s.length() ? s.substring(st, len) : s;
	}
	public static String Enum2Num(String prevalue) 
	{
		BigDecimal bd = new BigDecimal(prevalue);
		return bd.toPlainString();
	}
	
	public static boolean isNullStr(String str)
	{
		if(str == null || str.trim().length() == 0)return true;
		return false;
	}
//	public static String bin2hex(byte[] bytes) {
//		return bin2hex(bytes);
//	}
	
	public static boolean splitByFont(String text, List<String> texts)
	{
		if (text == null) return false;
		
		StringBuffer sbText = new StringBuffer("");
		StringBuffer sbTextb = new StringBuffer("");
		int len = text.length();
		for (int i = 0; i < len; i = text.offsetByCodePoints(i, 1))
		{
			int cInt = text.codePointAt(i);
			String prtStr = new String(Character.toChars(cInt));
			
			if (cInt > 65535)
			{
				sbText.append("  ");
				sbTextb.append(prtStr);
			}
			else
			{
				int num = StringUtil.getChineseLength(prtStr);
				for (int c = 0; c < num; c++) sbTextb.append(" ");
				sbText.append(prtStr);
			}
		}
		texts.add(sbText.toString());
		if (sbTextb.length() > 0)
		{
			texts.add(sbTextb.toString());
			return true;
		}
		return false;
	}
	
	
	public static String trimMore(String text)
	{
		if (text == null) return text;
		
		StringBuffer sbText = new StringBuffer("");
		for (int i = 0; i < text.length(); i++)
		{
			if (i == 0 || 
					text.charAt(i) != ' '|| 
					text.charAt(i) == ' ' && text.charAt(i - 1) != ' ') sbText.append(text.charAt(i));
		}
		
		return sbText.toString();
	}
}