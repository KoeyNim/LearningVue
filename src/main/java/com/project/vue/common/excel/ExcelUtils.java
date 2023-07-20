package com.project.vue.common.excel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.project.vue.common.excel.annotation.ExcelOptions;

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

		/** ExcelOptions의 데이터 추출 (Super Class 포함) */
		ReflectionUtils.doWithFields(cls, field -> {
			if (field.isAnnotationPresent(ExcelOptions.class)) {
				ExcelOptions opt = field.getAnnotation(ExcelOptions.class);
				ExcelDTO resource = ExcelDTO.builder()
						.headerNm(opt.headerName())
						.headerStyle(opt.headerStyle())
						.colNm(field.getName().substring(0,1).toUpperCase() + field.getName().substring(1))
						.colWidth(opt.columnWidth())
						.colStyle(opt.columnStyle()).build();
				resources.add(resource);
			}
		});
		return resources;
	}
}
