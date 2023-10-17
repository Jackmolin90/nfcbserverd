package com.imooc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * sha256
 */
@Slf4j
public class HmacSha256Util {

	/**
	 * 
	 *@param data
	 *@param key
	 *@return
	 *@throws Exception
	 *@String
	 */
	public static String HMACSHA256(String data, String key) throws Exception {

	       Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

	       SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");

	       sha256_HMAC.init(secret_key);

	       byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

	       StringBuilder sb = new StringBuilder();

	       for (byte item : array) {

	           sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));

	       }

	       return sb.toString().toUpperCase();

	   }


	public static  String getSign(String data,String key) throws  Exception{
		return HMACSHA256(DigestUtils.md5Hex(data), key);
	}


	public static void main(String[] args) throws  Exception{
		String sign_data ="appid=123456&random=123&address="+Constants.PRE+"7a4539ed8a0b8b4583ead1e5a3f604e83419f502";
		log.info(DigestUtils.md5Hex(sign_data));
		String sign_key = "7f7a0d20-25bb-11ec-9528-b3ab6021c264";
		String sign = HmacSha256Util.getSign(sign_data,sign_key);
		log.info(sign);
	}
}
