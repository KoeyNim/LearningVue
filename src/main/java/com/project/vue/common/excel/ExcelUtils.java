package com.project.vue.common.excel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.project.vue.common.excel.annotation.ExcelColumnOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExcelUtils {

	/**
	 * Entity 에서 지정한 옵션값을 추출 하여 가공하는 Method
	 * @param cls Entity class
	 * @return ExcelDTO
	 */
	public static List<ExcelDTO> getResources(Class<?> cls) {
		log.debug("@@ ExcelUtils - getResource - Entity : {}", cls.getName());
		List<ExcelDTO> resources = new ArrayList<>();

		/** ExcelColumnOptions의 데이터 추출 (Super Class 포함) */
		ReflectionUtils.doWithFields(cls, field -> {
			if (field.isAnnotationPresent(ExcelColumnOptions.class)) {
				ExcelColumnOptions annotation = field.getAnnotation(ExcelColumnOptions.class);
				ExcelDTO resource = ExcelDTO.builder()
						.headerNm(annotation.headerName())
						.colNm(field.getName().substring(0,1).toUpperCase() + field.getName().substring(1))
						.colWidth(annotation.columnWidth())
						.colStyle(annotation.columnStyle()).build();
				resources.add(resource);
			}
		});
		return resources;
	}
}
