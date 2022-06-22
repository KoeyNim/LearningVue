package com.project.vue.common.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.poi.ss.usermodel.BorderStyle;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumnName {
	String headerName() default "";
	BorderStyle ColumnStyle() default BorderStyle.THIN;
//	Font ColumnFont() default ;
}
