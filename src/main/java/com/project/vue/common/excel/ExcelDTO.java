package com.project.vue.common.excel;

import org.apache.poi.ss.usermodel.BorderStyle;

import lombok.Builder;
import lombok.Getter;

@Getter 
@Builder
public class ExcelDTO {

	/** header 명*/
	private String headerNm;
	/** header 스타일 */
	private BorderStyle headerStyle;
	/** column field 명*/
	private String colNm;
	/** column 스타일 */
	private BorderStyle colStyle;
	/** column Width */
	private int colWidth;
}
