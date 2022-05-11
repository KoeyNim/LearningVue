package com.project.vue.common;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelDownload {

	protected SXSSFWorkbook wb;
	protected SXSSFSheet sheet;
	protected int rowNo;

	public ByteArrayOutputStream buildExcelDocumentSXSSF(List<String> headerList, 
										         List<String> colList, 
										         List<?> dataList, 
										         String sheetName) throws Exception {
		rowNo = 0;
		wb = new SXSSFWorkbook();
		sheet = wb.createSheet(sheetName);
		
		renderHeaderRow(headerList);
		renderDataRow(colList, dataList);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		wb.setCompressTempFiles (true); // .csv 임시 파일 압축
		wb.write(stream);
		wb.close();

		return stream;
	}

	/**
	 *  헤더 Row 생성
	 */
	protected void renderHeaderRow(List<String> headerList) {
		SXSSFRow headerRow = sheet.createRow(rowNo++);
		int cellIdx = 0;
		for ( String header : headerList ) {
			SXSSFCell cell = headerRow.createCell(cellIdx++, CellType.STRING);
			cell.setCellValue(header);
		}
	}

	/**
	 * 데이터 Row 생성
	 */
	protected void renderDataRow(List<String> colList, List<?> dataList) {
		int cellIdx = 0;
		for ( Object data : dataList ) {
			SXSSFRow dataRow = sheet.createRow(rowNo++);
			
			for (String colLists : colList ) {
				renderDataCell(dataRow, cellIdx++, data, colLists);
			}
			cellIdx = 0;
		}
	}

	/**
	 * 데이터 Cell 매핑
	 */
	protected void renderDataCell(SXSSFRow dataRow, int cellIdx, Object data, String colList)
	{
		try {
			// 해당하는 method를 찾음
			SXSSFCell cell = dataRow.createCell(cellIdx);
			Method method = data.getClass().getMethod("get" + colList);
			// method 실행
			Object methodValue = method.invoke(data);
			
			log.debug("Type {}", method.getReturnType());
			
			// 값 삽입이 우선시 되어야 타입이 적용 됨.
			if (ObjectUtils.isNotEmpty(methodValue)) {
				if (String.class.equals(method.getReturnType())) {
					cell.setCellValue(methodValue.toString());
					return;
	//				cell.setCellType(CellType.STRING);
				}
				if (Long.class.equals(method.getReturnType())) {
					cell.setCellValue((Long)methodValue);
					return;
	//				cell.setCellType(CellType.NUMERIC);
				}
				if (Integer.class.equals(method.getReturnType())) {
					cell.setCellValue((Integer)methodValue);
					return;
				}
				if (LocalDate.class.equals(method.getReturnType())) {
					cell.setCellValue((LocalDate)methodValue);
					return;
				}
			return;
		} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
