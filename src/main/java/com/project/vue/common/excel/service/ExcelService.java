package com.project.vue.common.excel.service;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.project.vue.common.excel.ExcelUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelService<T> {

	private SXSSFWorkbook wb; // 쓰기전용이며 읽기 불가능
	private Map<String, Object> resource;
	private SXSSFSheet sheet;
	private List<T> dataList;
	private int rowNo;
	private CellStyle dataCellStyle;
	
	public ExcelService(List<T> dataList, Class<T> type) {
		this.wb = new SXSSFWorkbook(100); // flush 범위 (기본값 100, -1 일 경우 제한 없음)
		this.resource = ExcelUtils.getResource(type);
		this.dataList = dataList;
		this.dataCellStyle = wb.createCellStyle();
	}

	public ResponseEntity<ByteArrayResource> downloadExcel() throws Exception {
		try {
			rowNo = 0;
			
			String sheetName = (String)resource.get("fileName");
			
			@SuppressWarnings("unchecked")
			List<String> headerList = (List<String>)resource.get("headerList");
			
			@SuppressWarnings("unchecked")
			List<String> colList = (List<String>)resource.get("colList");
			
			sheet = wb.createSheet(sheetName);
			
			renderHeaderRow(headerList);
			renderDataRow(colList, dataList);
//			autoSizeColumns();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			wb.write(stream);
			wb.dispose();
			wb.close();
	    	String fileName = sheetName+"_"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))+".xlsx";
			String orgFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
	
			return ResponseEntity.ok()
					 //attachement = 로컬에 저장, filename = 다운로드시 파일 이름 지정 
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=" + orgFileName +";")
					.header(HttpHeaders.CONTENT_TYPE, "ms-vnd/excel") 
					.body(new ByteArrayResource(stream.toByteArray()));
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ByteArrayResource>(HttpStatus.CONFLICT);
		}
	}

	/**
	 *  헤더 Row 생성
	 */
	private void renderHeaderRow(List<String> headerList) {
		SXSSFRow headerRow = sheet.createRow(rowNo++);
		int cellIdx = 0;
		for ( String header : headerList ) {
			SXSSFCell cell = headerRow.createCell(cellIdx++, CellType.STRING);
			cell.setCellValue(header);
			cell.setCellStyle(makeHeaderCellStyle());
		}
	}

	/**
	 * 데이터 Row 생성
	 */
	private void renderDataRow(List<String> colList, List<T> dataList) {
		int cellIdx = 0;
		for ( T data : dataList ) {
			SXSSFRow dataRow = sheet.createRow(rowNo++);
			for (String colLists : colList ) {
				renderDataCell(dataRow, cellIdx++, data, colLists);
			}
			cellIdx = 0;
		}
	}

	/**
	 * 데이터 Cell 매핑
	 * CellStyle 복제 (excel 2007 기준 64000개가 넘어갈 경우 에러 발생)
	 */
	private void renderDataCell(SXSSFRow dataRow, int cellIdx, T data, String colList)
	{
		try {
			// 해당하는 method를 찾음
			SXSSFCell cell = dataRow.createCell(cellIdx);
			Method method = data.getClass().getMethod("get" + colList);
			// method 실행
			Object methodValue = method.invoke(data);
			
			if (java.lang.Integer.class.isInstance(methodValue)) {
				cell.setCellValue((Integer)methodValue);
				cell.setCellStyle(makeDataCellStyle());
				return;
			}
			if (java.lang.Long.class.isInstance(methodValue)) {
				cell.setCellValue((Long)methodValue);
				cell.setCellStyle(makeDataCellStyle());
				return;
			}
			if (java.time.LocalDate.class.isInstance(methodValue)) {
				cell.setCellValue((LocalDate)methodValue);
				cell.setCellStyle(makeDataCellStyle());
				return;
			}
			cell.setCellValue(ObjectUtils.isEmpty(methodValue) ? "" : methodValue.toString());
			cell.setCellStyle(makeDataCellStyle());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 헤더 Cell 스타일
	 */
	private CellStyle makeHeaderCellStyle()
	{
		CellStyle headerCellStyle = wb.createCellStyle();
		
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		
		headerCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		headerCellStyle.setBorderRight(BorderStyle.MEDIUM);
		headerCellStyle.setBorderTop(BorderStyle.MEDIUM);
		headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		
		Font font = wb.createFont();
		font.setBold(true);
		
		headerCellStyle.setFont(font);
		
		return headerCellStyle;
	}
	
	/**
	 * 데이터 Cell 스타일
	 */
	private CellStyle makeDataCellStyle()
	{
		return makeDataCellStyle(null);
	}
	
	/**
	 * 데이터 Cell 스타일
	 */
	private CellStyle makeDataCellStyle(HorizontalAlignment halign)
	{
		if (ObjectUtils.isNotEmpty(halign))
			dataCellStyle.setAlignment(halign);
		
		dataCellStyle.setBorderLeft(BorderStyle.THIN);
		dataCellStyle.setBorderRight(BorderStyle.THIN);
		dataCellStyle.setBorderTop(BorderStyle.THIN);
		dataCellStyle.setBorderBottom(BorderStyle.THIN);
		
		Font font = wb.createFont();
		
		dataCellStyle.setFont(font);
		
		return dataCellStyle;
	}
	
	
	// flush 제한을 걸 경우 SXSSFRow는 쓰기 전용이라 getRow 메소드를 사용할 수 없으므로 null값이 나옴.
	// Column 사이즈 조절 솔루션 : 커스텀 어노테이션으로 각 entity 에 default와 
	// 전체 default 그리고 커스텀이 가능한 인터페이스를 만든다.
	private void autoSizeColumns() {
		if (sheet.getPhysicalNumberOfRows() > 0) {
			SXSSFRow row = sheet.getRow(sheet.getFirstRowNum());
			Iterator<Cell> cellIterator = row.cellIterator();
//			sheet.trackAllColumnsForAutoSizing(); // 모든 컬럼을 찾은 후 자동 사이즈 조절 (속도저하)
			while (cellIterator.hasNext()) {
				int columnIndex = cellIterator.next().getColumnIndex();
				int currentColumnWidth = sheet.getColumnWidth(columnIndex);
				sheet.setColumnWidth(columnIndex, (currentColumnWidth + 2500));
			}
		}
	}
}
