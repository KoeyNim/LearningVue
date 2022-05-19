package com.project.vue.common;

import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Convert
public class StringCryptoConverter implements AttributeConverter<String, String> {
	
	@Value("${site.secretKey}")
	private String Secret_KEY;

	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (StringUtils.isBlank(attribute)) {
			throw new NullPointerException();
		}
		Key key = new SecretKeySpec(Secret_KEY.getBytes(), "AES");
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return new String(Hex.encode(cipher.doFinal(attribute.getBytes("UTF-8"))));
//			return new String(Base64Utils.encode(cipher.doFinal(attribute.getBytes("UTF-8"))));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		Key key = new SecretKeySpec(Secret_KEY.getBytes(), "AES");
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(Hex.decode(dbData)), "UTF-8");
//			return new String(cipher.doFinal(Base64Utils.decode(dbData.getBytes("UTF-8"))));
		} catch (BadPaddingException e) {
			log.info("Incorrect secret key");
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
