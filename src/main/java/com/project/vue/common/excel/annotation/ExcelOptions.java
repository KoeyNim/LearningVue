package com.project.vue.common.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.poi.ss.usermodel.BorderStyle;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelOptions {
	/**
	 * header 명
	 */
	String headerName() default "";
	/**
	 * header 스타일 지정
	 */
	BorderStyle headerStyle() default BorderStyle.MEDIUM;
	/**
	 * Column 스타일 지정
	 */
	BorderStyle columnStyle() default BorderStyle.THIN;
	/**
	 * Column 너비 설정
	 */
	int columnWidth() default 6000;
}
