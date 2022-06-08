package com.project.vue.common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import com.project.vue.common.excel.annotation.ExcelColumnName;
import com.project.vue.common.excel.annotation.ExcelFileName;

@Component
public class Utils {

	/**
	 *  Class 안에 있는 변수명을 받음.
	 */
	public static List<String> getColList(Class<?> Entity) {
		List<String> list = Arrays
								.stream(Entity.getDeclaredFields())
								.parallel() // 병렬처리
				                .map(entity -> entity.getName().substring(0,1).toUpperCase() + entity.getName().substring(1))
				                .collect(Collectors.toList());
//		ArrayList<String> list = new ArrayList<>();
//		for (Field lists : Entity.getDeclaredFields()) {
//			String firstUpper = lists.getName().substring(0,1).toUpperCase() + lists.getName().substring(1);
//			list.add(firstUpper);
//		}
		return list;
	}
	
	public static Map<String, Object> getResource(Class<?> Entity) {
		
		Map<String, Object> resource = new HashedMap<String, Object>();
		
		String fileName = Entity.getAnnotation(ExcelFileName.class).fileName();
		
		String headerList = Entity.getAnnotation(ExcelColumnName.class).headerName();
		
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
