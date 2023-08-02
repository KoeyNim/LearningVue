package com.project.vue.common.converter;

import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;

import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.ErrorCode;

@Convert
public class StringCryptoConverter implements AttributeConverter<String, String> {

	/** 시크릿 키 */
	@Value("${site.crypto.secret-key}")
	private String secretKey;

	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * 암호화
	 */
	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (StringUtils.isBlank(attribute)) throw new BizException("Null" + attribute, ErrorCode.INTERNAL_SERVER_ERROR);

		try {
			Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return new String(Hex.encode(cipher.doFinal(attribute.getBytes("UTF-8"))));
//			return new String(Base64Utils.encode(cipher.doFinal(attribute.getBytes("UTF-8"))));
		} catch (BadPaddingException ex) {
			throw new BizException("Incorrect Secret Key", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
			throw new BizException("Convert To Database Column Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		} 
	}

	/**
	 * 복호화
	 */
	@Override
	public String convertToEntityAttribute(String dbData) {
		try {
			Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(Hex.decode(dbData)), "UTF-8");
//			return new String(cipher.doFinal(Base64Utils.decode(dbData.getBytes("UTF-8"))));
		} catch (BadPaddingException ex) {
			throw new BizException("Incorrect Secret Key", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
			throw new BizException("Convert To Entity Attribute Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		} 
	}

}
