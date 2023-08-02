package com.project.vue.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import org.apache.commons.lang3.StringUtils;

import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.ErrorCode;

@Convert
public class GenderConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return attribute;
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (StringUtils.equals(dbData, "M")) return "남자";
		if (StringUtils.equals(dbData, "W")) return "여자";
		throw new BizException("Gender Converter Error", ErrorCode.INTERNAL_SERVER_ERROR);
	}

}
