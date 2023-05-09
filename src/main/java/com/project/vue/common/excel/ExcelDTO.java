package com.project.vue.common.excel;

import org.apache.poi.ss.usermodel.BorderStyle;

import lombok.Builder;
import lombok.Getter;

@Getter 
@Builder
public class ExcelDTO {

	/** header 명*/
	private String headerNm;
	/** column field 명*/
	private String colNm;
	/** column Width */
	private int colWidth;
	/** column 스타일 */
	private BorderStyle colStyle;
}
