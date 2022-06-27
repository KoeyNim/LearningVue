package com.project.vue.common.excel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.stereotype.Component;

import com.project.vue.common.excel.DTO.ExcelDTO;
import com.project.vue.common.excel.annotation.ExcelColumnOptions;
import com.project.vue.common.excel.annotation.ExcelFileName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExcelUtils {

	// Entity Class 데이터를 추출 및 재 가공
	public static ExcelDTO getResource(Class<?> Entity) {

		ExcelDTO resource = new ExcelDTO();
		List<String> headerList = new ArrayList<>();
		List<String> colList = new ArrayList<>();
		List<BorderStyle> colStyle = new ArrayList<>();

		// @ExcelColumnOptions의 데이터 추출
		for (Field field : Entity.getDeclaredFields()) {
			// @ExcelColumnOptions를 가지고 있는 필드만 로직 실행
			if (field.isAnnotationPresent(ExcelColumnOptions.class)) {
	            headerList.add(field.getAnnotation(ExcelColumnOptions.class).headerName());
	            colList.add(field.getName().substring(0,1).toUpperCase() + field.getName().substring(1));
	            colStyle.add(field.getAnnotation(ExcelColumnOptions.class).ColumnStyle());
			}
		}

		// @ExcelColumnOptions의 fileName 데이터 추출
		resource.setFileName(Entity.isAnnotationPresent(ExcelFileName.class) 
								? Entity.getAnnotation(ExcelFileName.class).fileName() 
								: "미지정");
		resource.setHeaderList(headerList);
		resource.setColList(colList);
		resource.setColStyle(colStyle);
		
		log.debug("resource : {}", resource);
		log.debug("fileName : {}", resource.getFileName());
		log.debug("headerList : {}", resource.getHeaderList());
		log.debug("colList : {}", resource.getColList());
		log.debug("colStyle : {}", resource.getColStyle());

		return resource;
	}
}
