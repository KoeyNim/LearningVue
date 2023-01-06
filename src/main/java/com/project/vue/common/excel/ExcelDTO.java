package com.project.vue.common.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;

import lombok.Builder;
import lombok.Getter;

@Getter 
@Builder
public class ExcelDTO {
	private String excelNm;
	private List<String> headerList;
	private List<String> colList;
	private List<BorderStyle> colStyle;
}
