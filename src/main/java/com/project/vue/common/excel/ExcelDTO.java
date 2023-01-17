package com.project.vue.common.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;

import lombok.Builder;
import lombok.Getter;

@Getter 
@Builder
public class ExcelDTO {

	/** 엑셀 파일명 */
	private String excelNm;
	/** header 리스트  */
	private List<String> headerList;
	/** column 리스트 */
	private List<String> colList;
	/** column 스타일 */
	private List<BorderStyle> colStyle;
}
