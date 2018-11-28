package com.free.utils.encrypt;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


  
/** *//** 
 * <p> 
 * RSA公钥/私钥/签名工具包 
 * </p> 
 * <p> 
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman） 
 * </p> 
 * <p> 
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/> 
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/> 
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全 
 * </p> 
 *  
 */  
public class CxCryptRSAUtils {  
  
    /** *//** 
     * 加密算法RSA 
     */  
    public static final String KEY_ALGORITHM = "RSA";  
      
    /** *//** 
     * 签名算法 
     */  
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";  
  
    /** *//** 
     * 获取公钥的key 
     */  
    private static final String PUBLIC_KEY = "RSAPublicKey";  
      
    /** *//** 
     * 获取私钥的key 
     */  
    private static final String PRIVATE_KEY = "RSAPrivateKey";  
      
    /** *//** 
     * RSA最大加密明文大小 
     */  
    private static final int MAX_ENCRYPT_BLOCK = 117;  
      
    /** *//** 
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 128;  
  
    /** *//** 
     * <p> 
     * 生成密钥对(公钥和私钥) 
     * </p> 
     *  
     * @return 
     * @throws Exception 
     */  
    public static Map<String, Object> genKeyPair() throws Exception {  
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);  
        keyPairGen.initialize(1024);  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
        keyMap.put(PUBLIC_KEY, publicKey);  
        keyMap.put(PRIVATE_KEY, privateKey);  
        return keyMap;  
    }  
      
    /** *//** 
     * <p> 
     * 用私钥对信息生成数字签名 
     * </p> 
     *  
     * @param data 已加密数据 
     * @param privateKey 私钥(BASE64编码) 
     *  
     * @return 
     * @throws Exception 
     */  
    public static String sign(byte[] data, String privateKey) throws Exception {  
        byte[] keyBytes = Base64Util.decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initSign(privateK);  
        signature.update(data);  
        return Base64Util.encode(signature.sign());  
    }  
  
    /** *//** 
     * <p> 
     * 校验数字签名 
     * </p> 
     *  
     * @param data 已加密数据 
     * @param publicKey 公钥(BASE64编码) 
     * @param sign 数字签名 
     *  
     * @return 
     * @throws Exception 
     *  
     */  
    public static boolean verify(byte[] data, String publicKey, String sign)  
            throws Exception {  
        byte[] keyBytes = Base64Util.decode(publicKey);  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PublicKey publicK = keyFactory.generatePublic(keySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initVerify(publicK);  
        signature.update(data);  
        return signature.verify(Base64Util.decode(sign));  
    }  
  
    /** *//** 
     * <P> 
     * 私钥解密 
     * </p> 
     *  
     * @param encryptedData 已加密数据 
     * @param privateKey 私钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)  
            throws Exception {  
        byte[] keyBytes = Base64Util.decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段解密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }  
  
    /** *//** 
     * <p> 
     * 公钥解密 
     * </p> 
     *  
     * @param encryptedData 已加密数据 
     * @param publicKey 公钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)  
            throws Exception {  
        byte[] keyBytes = Base64Util.decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, publicK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段解密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }  
  
    /** *//** 
     * <p> 
     * 公钥加密 
     * </p> 
     *  
     * @param data 源数据 
     * @param publicKey 公钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)  
            throws Exception {  
        byte[] keyBytes = Base64Util.decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0)
        { 
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }  
  
    /** *//** 
     * <p> 
     * 私钥加密 
     * </p> 
     *  
     * @param data 源数据 
     * @param privateKey 私钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)  
            throws Exception {  
        byte[] keyBytes = Base64Util.decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }  
  
    /** *//** 
     * <p> 
     * 获取私钥 
     * </p> 
     *  
     * @param keyMap 密钥对 
     * @return 
     * @throws Exception 
     */  
    public static String getPrivateKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PRIVATE_KEY);  
        return Base64Util.encode(key.getEncoded());  
    }  
  
    /** *//** 
     * <p> 
     * 获取公钥 
     * </p> 
     *  
     * @param keyMap 密钥对 
     * @return 
     * @throws Exception 
     */  
    public static String getPublicKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PUBLIC_KEY);  
        return Base64Util.encode(key.getEncoded());  
    }  
    public static void main(String[] args) throws Exception
	{
    	//非对称密钥对加密解密
    	String publicKey=null;  
    	String privateKey=null;  
    	  
        try {  
            Map<String, Object> keyMap = CxCryptRSAUtils.genKeyPair();  
            publicKey = CxCryptRSAUtils.getPublicKey(keyMap);  
            privateKey = CxCryptRSAUtils.getPrivateKey(keyMap);  
            System.err.println("公钥: \n\r" + publicKey);  
            System.err.println("私钥： \n\r" + privateKey);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
    	
    	
    	 System.err.println("公钥加密——私钥解密");  
//         String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？ooooo";  
         String source = "RDP_USERDATA=RDP_TELLERID=980098#!@!*@RDP_BRANCHID=#!@!*@RDP_PASSWORD=#!@!*@RDP_IDNO=#!@!*@RDP_ENTITYID=9999#!@!*@RDP_PRENO=#!@!*@RDP_USERVERSION=|*#!#*@|RDP_SYSDATA=RDP_TASKID=rdpssologinin#!@!*@RDP_ACTIONTYPE=trxsubmit#!@!*@RDP_FORMID=#!@!*@RDP_CLIENTNO=SN010101#!@!*@RDP_CLIENTIP=10.10.80.9#!@!*@RDP_CLIENTHOSTNAME=orange-PC#!@!*@RDP_WARNINGCONFIRM=#!@!*@RDP_TRXDATE=#!@!*@RDP_SYSTEMID=core#!@!*@RDP_CONNECTSYSTEMID=core#!@!*@RDP_RDPREF=980098000164657002#!@!*@RDP_MAIN_TASKID=#!@!*@RDP_HEJIJINE=#!@!*@RDP_CJSFSVRLST=#!@!*@RDP_FEIYQRBZ=#!@!*@RDP_CPLFYXINX=#!@!*@RDP_TELLERMEDIACHECKFLAG=#!@!*@RDP_AUTHGUIYZWEN=#!@!*@RDP_AUTHGUIYZWBS=#!@!*@RDP_JIOYZWEN=#!@!*@RDP_JIOYZWBS=#!@!*@RDP_ERCDXINX=#!@!*@RDP_TELLERDYNAMICNO=#!@!*@RDP_TELLERDYNAMICPASSWORD=#!@!*@RDP_ZONGBSHU=#!@!*@RDP_VERIFYSEALRESULT=#!@!*@RDP_IDCARDCHECKRESULT=#!@!*@RDP_LDBIAOZI=#!@!*@RDP_LDRDPREF=#!@!*@RDP_SQQRENBZ=#!@!*@RDP_MAINRDPREF=#!@!*@RDP_SHOUQMAA=#!@!*@RDP_ICCARDBRUSHFLAG=#!@!*@RDP_ICCARDNOFIELD=#!@!*@RDP_ICCARDREADSCRIPT=#!@!*@RDP_ICCARDORDER=#!@!*@RDP_ICCARDWRITESCRIPT=#!@!*@RDP_SOPAUTHREF=#!@!*@RDP_SOPAUTHTELLER=#!@!*@RDP_SOPAUTHPASSWORD=#!@!*@RDP_SOPAUTHDYNAMICPASSWORD=#!@!*@RDP_SOPAUTHLEVEL=#!@!*@RDP_SOPAUTHMETHOD=#!@!*@RDP_SOPAUTHBRANCH=#!@!*@RDP_SOPAUTHBRANCHLEVEL=#!@!*@RDP_SOPAUTHMESSAGE=#!@!*@RDP_SOPAUTHFLAG=#!@!*@RDP_SOPAUTHREMOTEFLAG=#!@!*@RDP_SOPAUTHSHOQQRGY=#!@!*@RDP_SOPAUTHSHOQQRMM=#!@!*@RDP_SOPAUTHSHOQQRZW=#!@!*@RDP_SOPAUTHSHOQQRDYNAMICPASSWORD=#!@!*@RDP_SOPAUTHBENDSQCS=|*#!#*@|RDP_TRXDATA=guiymima=111111#!@!*@guiydaih=980098#!@!*@farendma=9999#!@!*@entityid=9999#!@!*@|*#!#*@|RDP_REQUESTTYPE=1|*#!#*@|RDP_FLEXSEQ=980098000164657003|*#!#*@|RDP_MAC=|*#!#*@|RDP_TRXDETAIL=|*#!#*@|RDP_FIELDTYPE=|*#!#*@|RDP_FRONTCONFIG=|*#!#*@|RDP_PWDKEYDATA=|*#!#*@|RDP_EXTENDDATA=|*#!#*@|RDP_CRYPTVERSION=|*#!#*@|RDP_CRYPTTYPE=RSA|*#!#*@|RDP_PUBLICKEY=MIGAMA0GCSqGSIb3DQEBAQUAA28AMGwCZQCXfsOF53bG85PVl/XET6T1ickLmTziGtyjgCG6J7Q4x0dq89bffOhG4M36SW5GJNCTOShJ/mWwhmt8nRAXMCfhKwna+dsZTTfrEaqR3u03xEUTXzIMJ8cWwsGweM6gRUP6nihfAgMBAAE=";  
         System.out.println("\r加密前文字：\r\n" + source);  
         byte[] data = source.getBytes();  
         byte[] encodedData = encryptByPublicKey(data, publicKey);  
         System.out.println("加密后文字：\r\n" + new String(Base64Util.encode(encodedData)));  
         
         byte[] decodedData = CxCryptRSAUtils.decryptByPrivateKey(encodedData, privateKey);  
         String target = new String(decodedData);  
         System.out.println("解密后文字: \r\n" + target);
         
         
         //数字签名
         
//         System.err.println("私钥加密——公钥解密");  
//         String source = "这是一行测试RSA数字签名的无意义文字";  
//         System.out.println("原文字：\r\n" + source);  
//         byte[] data = source.getBytes();  
//         byte[] encodedData = CxCryptRSAUtils.encryptByPrivateKey(data, privateKey);  
//         System.out.println("加密后：\r\n" + new String(encodedData));  
//         byte[] decodedData = CxCryptRSAUtils.decryptByPublicKey(encodedData, publicKey);  
//         String target = new String(decodedData);  
//         System.out.println("解密后: \r\n" + target);  
//         System.err.println("私钥签名——公钥验证签名");  
//         String sign = CxCryptRSAUtils.sign(encodedData, privateKey);  
//         System.err.println("签名:\r" + sign);  
//         boolean status = CxCryptRSAUtils.verify(encodedData, publicKey, sign);  
//         System.err.println("验证结果:\r" + status);  
		
	}
} 