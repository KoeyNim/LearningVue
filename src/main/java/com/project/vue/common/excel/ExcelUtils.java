package com.project.vue.common.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.project.vue.common.excel.annotation.ExcelColumnOptions;
import com.project.vue.common.excel.annotation.ExcelFileName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExcelUtils {

	/**
	 * Entity 에서 지정한 옵션값을 추출 하여 가공하는 Method
	 * @param cls Entity class
	 * @return ExcelDTO
	 */
	public static ExcelDTO getResource(Class<?> cls) {
		log.debug("@@ ExcelUtils - getResource - Entity : {}", cls.getName());

		List<String> headerList = new ArrayList<>();
		List<String> colList = new ArrayList<>();
		List<BorderStyle> colStyle = new ArrayList<>();

		/** ExcelColumnOptions의 데이터 추출 (Super Class 포함) **/
		ReflectionUtils.doWithFields(cls, field -> {
			if (field.isAnnotationPresent(ExcelColumnOptions.class)) {
	            headerList.add(field.getAnnotation(ExcelColumnOptions.class).headerName());
	            colList.add(field.getName().substring(0,1).toUpperCase() + field.getName().substring(1));
	            colStyle.add(field.getAnnotation(ExcelColumnOptions.class).ColumnStyle());
			}
		});

		ExcelDTO resource = ExcelDTO.builder()
								.excelNm(cls.isAnnotationPresent(ExcelFileName.class) 
											? cls.getAnnotation(ExcelFileName.class).fileName() 
										    : "미지정")
								.headerList(headerList)
								.colList(colList)
								.colStyle(colStyle).build();

		log.debug("excelNm : {}", resource.getExcelNm());
		log.debug("headerList : {}", resource.getHeaderList());
		log.debug("colList : {}", resource.getColList());
		log.debug("colStyle : {}", resource.getColStyle());

		return resource;
	}
}
