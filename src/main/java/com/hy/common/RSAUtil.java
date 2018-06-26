package com.hy.common;


import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
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

public class RSAUtil {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";//SHA1WithRSA 或者 MD5withRSA

	public static final String PUBLIC_KEY = "RSAPublicKey";
	public static final String PRIVATE_KEY = "RSAPrivateKey";

	
	public static byte[] decryptBASE64(String key) throws Exception {
		//return (new BASE64Decoder()).decodeBuffer(key);
		return Base64.decodeBase64(key.getBytes());
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		//return (new BASE64Encoder()).encodeBuffer(key);
		return new String(Base64.encodeBase64(key));
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);

		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);

		return encryptBASE64(signature.sign());
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * 
	 * @return 校验成功返回true 失败返回false
	 * @throws Exception
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {

		// 解密由base64编码的公钥
		byte[] keyBytes = decryptBASE64(publicKey);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);

		// 验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	/**
	 * 解密<br>
	 * 用公钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	/*public static byte[] decryptByPublicKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}*/

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key)
			throws Exception {
		// 对公钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}

	
	
	/**
	 * 加密<br>
	 * 用私钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	/*public static byte[] encryptByPrivateKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}*/

	/**
	 * 取得私钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);

		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);

		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
	
	public static void main(String[] args) throws Exception {
//		Map<String, Object> map=RSAUtil.initKey();
//		String publicKey=RSAUtil.getPublicKey(map);
//		String privateKey=RSAUtil.getPrivateKey(map);
//		System.out.println("PublicKey:"+publicKey);
//		System.out.println("privateKey:"+privateKey);
		//给游戏方
		String publicKey_U="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgSVsTbqO+vKwiwpihq84wxkQBf2uPvB1yeWOtFOfGid7Xn0UR5UuctQbxCPiDXTWp6Pfa+uHHFiqGTpHLS2jiBZvl7fw4aj28AMxOLpQseU6BLPECX7MR4MNnTEl5CP4gS8s6iojsXP1XSm6ycD0RqnNjTeEZuJpfZF/soOSLVQIDAQAB";
		String privateKey_U="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKBJWxNuo768rCLCmKGrzjDGRAF/a4+8HXJ5Y60U58aJ3tefRRHlS5y1BvEI+INdNano99r64ccWKoZOkctLaOIFm+Xt/DhqPbwAzE4ulCx5ToEs8QJfsxHgw2dMSXkI/iBLyzqKiOxc/VdKbrJwPRGqc2NN4Rm4ml9kX+yg5ItVAgMBAAECgYEAjjmI/u0ljpGLjCxvksUH6G+E2G9f2eJpnrHnqCTspFahzWIdv4teVGatCiOTn7yNaX62ry/+NtGMWjBNXb5ECSIFXAbORBlBVza895yuS4xNZrVbSOagSp6V2QIemhUSDd06qRBbieYxQ0NqvI9/YpW8jwonbtux8OwCa/dxTeECQQDYCvEhz2aXGEqIQ4ddFCqI5DKKozo11A7DCg8rBlgWO30Zkwd3N+tHsHI2/zMR4O39MoVV9wxGnioLgBPwFOYjAkEAve6BBHqId7tCPjrhrra8RDHf5XS0m+/pTmSujh94M9M2VyAwNaI+CbwkaZ928Q+Z859xFHKWpBjSAY3b4yZUJwJBAL5NvSR8LjFQS28GlPlvpxwYb1DiAYei+THi7YOHvhYvZr4dXoksr8rhaWp6mADasqjDVOJpkN2vuveQZX+o2SMCQEOMa344RgivJKQVNOZ6PwLR6hslIobDEGq9DAen0Yw89fqOjBbHGOFFdH3MJPUe41Xtwr3O8xxxZ17+INHsc8kCQQCq5cn6RNH8akbXDmpkC42tfwtDRDd1rNd+yybY7SB8peeR+bbzUs2q1OV9btQJ+sG0WIgSZExMVpTdBKUn1FnX";
		String publicKey_D="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCX9BfeYShjlc/MD7WFuAHS0Erhg8DtQ7S0jn+Tftw5Hm5geXYeZg5wW6iWmmSeqmN2V0WyhNC2uK3CTcSveB9L+lZgJ1wSsPAUBxkyg4PLvOioywuwSnUqog1ootp/TFJO2Xx8KwUQVQkz1h/41Sset2lriq7ATSOPC2DG5+In+QIDAQAB";
		//给游戏方
		String privateKey_D="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJf0F95hKGOVz8wPtYW4AdLQSuGDwO1DtLSOf5N+3DkebmB5dh5mDnBbqJaaZJ6qY3ZXRbKE0La4rcJNxK94H0v6VmAnXBKw8BQHGTKDg8u86KjLC7BKdSqiDWii2n9MUk7ZfHwrBRBVCTPWH/jVKx63aWuKrsBNI48LYMbn4if5AgMBAAECgYAMC4pU02jLNBXrvBcMiYCDgIVfCP2jESni4iTUwUqdsH7ZYOdvE3HzkCB2B7kQJruJpvNcalDNGyiS7W0455G7aYU1bdazS/LdJywX5rAHtlaCm0LqYYZ9+it5K+QjV74of+StRJn1Mkr4Eg0FRPB/2v8+EE3+lJcdZKHmsUZ4MQJBAOJG2knYbgAicKGDO8jQV3/s2wS56cKg+sWheNVKm70bFBVCxs8u9uEgdhp4HATKgc6Txu3AmXeDC9aqDr9kXZUCQQCr6e0ZxmiWrY2dNWydCfd/QHN1ht2ezHmMa8Wm9kcCNM2QRw4Lsuy+wScjVRvXuzI/Q3sko7K00qEUt7z6jl/VAkEAvulEt1VRye9E1CCjSlTt3lL6n0w0fC/SJPiNuHeQWscD9MQ4dQmM5ni67K42BZzOVHq+Nk5vrTP1wxIMC1Mk2QJABq5hs5W9bNWz4j84SGYhw0VK929VX52Mv8NUfCeoHNLWhlvslNHQV52EGuBs45Z33nON7p+aC2MANaDqbEZqPQJADChTx5DojECQdOnYJT/fwQxb9HIuHxgb+maejN6hwMTBTW19wt8gSKT+AUp/vT1TItL3oVj/X1sH7l0XFZklXQ==";


		//--------商城向游戏方请求-----------
		System.out.println("商城向游戏方请求----->");
		String orgiPassword="123456";
		String password=RSAUtil.encryptBASE64(RSAUtil.encryptByPublicKey(orgiPassword.getBytes("UTF-8"),publicKey_D));

		String srcString="username=wdg_203232&password="+password;
		System.out.println("orgiPassword:"+orgiPassword);
		System.out.println("Source String:"+srcString);

		String signature=RSAUtil.sign(srcString.getBytes("UTF-8"), privateKey_U);
		System.out.println("Signature String:"+signature);

		//----------游戏方响应------------
		System.out.println("游戏方响应<-----------");
		if(!RSAUtil.verify(srcString.getBytes("UTF-8"), publicKey_U, signature)){
			System.out.println("Verify Failed");
			return;
		}
		System.out.println("Verify Passed");
		byte[] decPassowrd=RSAUtil.decryptBASE64(password);
		String newPassword=new String(RSAUtil.decryptByPrivateKey(decPassowrd,privateKey_D));
		System.out.println("newPassword:"+newPassword);


		//--------游戏方向商城请求-----------
		System.out.println("游戏方向商城请求--------->");
		String srcString1="amount=122.00&orderNo=2018030510000000001&currencyCode=HX";
		System.out.println("Source String:"+srcString1);

		String signature1=RSAUtil.sign(srcString1.getBytes("UTF-8"), privateKey_D);
		System.out.println("Signature String:"+signature1);

		//----------商城响应------------
		System.out.println("商城响应<-----------------");
		if(!RSAUtil.verify(srcString1.getBytes("UTF-8"), publicKey_D, signature1)){
			System.out.println("Verify Failed");
			return;
		}
		System.out.println("Verify Passed");





//
//
//		signature="PCQtpvWTbq8R0CrFfk%2FezTlcCQaHGqYpENgNXPfGShWDj5JhXASZ27pZnCdFHU9Y%2BZr%2FBdNDWKRj%0D%0A%2BTOStmnBMuVAZ50My0MttriaswlDWIluYgXkVsrg2lkS2aldJpaKlEdvXWCKr7eEIrffTf%2Fm18Bw%0D%0AfZyIvDwBmfC%2FKoZSFUs%3D%0D%0A";
//		signature=java.net.URLDecoder.decode(signature,"UTF-8");
//		if(RSAUtil.verify(srcString.getBytes("UTF-8"), publicKey, signature)){
//			System.out.println("Verify Passed");
//		}else{
//
//			System.out.println("Verify Failed");
//		}
	}

}
