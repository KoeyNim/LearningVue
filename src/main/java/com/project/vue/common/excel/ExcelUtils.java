package com.project.vue.common.excel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.stereotype.Component;

import com.project.vue.common.excel.DTO.ExcelDTO;
import com.project.vue.common.excel.annotation.ExcelColumnName;
import com.project.vue.common.excel.annotation.ExcelFileName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExcelUtils {

	// Entity Class 안에 있는 정보를 추출
	public static ExcelDTO getResource(Class<?> Entity) {

		ExcelDTO resource = new ExcelDTO();
		List<String> headerList = new ArrayList<>();

		// @ExcelColumnName의 headerName 값 추출 (다수)
		for (Field field : Entity.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelColumnName.class)) {
            	headerList.add(field.getAnnotation(ExcelColumnName.class).headerName());
            }
		}

		// Entity의 Fields명 추출 후 특정 문자열 대문자로 변환
		List<String> colList = Arrays
				.stream(Entity.getDeclaredFields())
				.parallel() // 병렬처리
				.filter(entity -> entity.isAnnotationPresent(ExcelColumnName.class)) // @ExcelColumnName가 있는 필드만 필터링
                .map(entity -> entity.getName().substring(0,1).toUpperCase() + entity.getName().substring(1)) // 재 가공
                .collect(Collectors.toList()); // 리스트 생성
		
		List<BorderStyle> colStyle = Arrays
				.stream(Entity.getDeclaredFields())
				.parallel() // 병렬처리
				.filter(entity -> entity.isAnnotationPresent(ExcelColumnName.class)) // @ExcelColumnName가 있는 필드만 필터링
				.map(entity -> entity.getAnnotation(ExcelColumnName.class).ColumnStyle()) // 재 가공
                .collect(Collectors.toList()); // 리스트 생성

		// @ExcelFileName의 fileName 값 추출
		resource.setFileName(Entity.getAnnotation(ExcelFileName.class).fileName());
		resource.setHeaderList(headerList);
		resource.setColList(colList);
		resource.setColStyle(colStyle);

		log.debug("fileName : {}", resource.getFileName());
		log.debug("headerList : {}", resource.getHeaderList());
		log.debug("colList : {}", resource.getColList());
		log.debug("colStyle : {}", resource.getColStyle());
		log.debug("colFont : {}", resource.getColFont());
		log.debug("colFont : {}", resource);

		return resource;
	}
}
