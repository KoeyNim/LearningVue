package com.project.vue.common.excel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import com.project.vue.common.excel.annotation.ExcelColumnName;
import com.project.vue.common.excel.annotation.ExcelFileName;

@Component
public class ExcelUtils {

	// Entity Class 안에 있는 정보를 추출
	public static Map<String, Object> getResource(Class<?> Entity) {
		
		// @ExcelFileName의 fileName 값 추출
		String fileName = Entity.getAnnotation(ExcelFileName.class).fileName();
		Map<String, Object> resource = new HashedMap<String, Object>();
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
                .map(entity -> entity.getName().substring(0,1).toUpperCase() + entity.getName().substring(1))
                .collect(Collectors.toList());
		
		resource.put("fileName", fileName);
		resource.put("headerList", headerList);
		resource.put("colList", colList);
		
		
		return resource;
	}
}
