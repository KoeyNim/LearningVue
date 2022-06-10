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

	private SXSSFWorkbook wb;
	private Map<String, Object> resource;
	private SXSSFSheet sheet;
	private List<T> dataList;
	private int rowNo;
	
	public ExcelService(List<T> dataList, Class<T> type) {
		this.wb = new SXSSFWorkbook(-1); // 허용 row 지정 (-1 일 경우 제한 없음)
		this.resource = ExcelUtils.getResource(type);
		this.dataList = dataList;
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
			autoSizeColumns();
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			wb.setCompressTempFiles (true); // .csv 임시 파일 압축
			wb.write(stream);
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
		CellStyle headerCellStyle = makeHeaderCellStyle();
		int cellIdx = 0;
		for ( String header : headerList ) {
			SXSSFCell cell = headerRow.createCell(cellIdx++, CellType.STRING);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}
	}

	/**
	 * 데이터 Row 생성
	 */
	private void renderDataRow(List<String> colList, List<?> dataList) {
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
	private void renderDataCell(SXSSFRow dataRow, int cellIdx, Object data, String colList)
	{
		try {
			// 해당하는 method를 찾음
			SXSSFCell cell = dataRow.createCell(cellIdx);
			Method method = data.getClass().getMethod("get" + colList);
			// method 실행
			Object methodValue = method.invoke(data);
			String methodReturnTypeStr = method.getReturnType().toString();
			String methodType = methodReturnTypeStr.substring(methodReturnTypeStr.lastIndexOf(".") + 1);
			// 값 삽입이 우선시 되어야 타입이 적용 됨.
			if (ObjectUtils.isNotEmpty(methodValue)) {
				switch (methodType) {
				case "String":
					cell.setCellValue(methodValue.toString());
					cell.setCellStyle(makeDataCellStyle());
					break;
				case "Long":
					cell.setCellValue((Long)methodValue);
					cell.setCellStyle(makeDataCellStyle());
					break;
				case "Integer":
					cell.setCellValue((Integer)methodValue);
					cell.setCellStyle(makeDataCellStyle());
					break;
				case "LocalDate":
					cell.setCellValue((LocalDate)methodValue);
					cell.setCellStyle(makeDataCellStyle());
					break;
				default:
					log.info("사용할 수 없는 클래스 타입 : {}",method.getReturnType());
					break;
				}
			}
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
		CellStyle dataCellStyle = wb.createCellStyle();
		if ( ObjectUtils.isNotEmpty(halign) )
			dataCellStyle.setAlignment(halign);
		
		dataCellStyle.setBorderLeft(BorderStyle.THIN);
		dataCellStyle.setBorderRight(BorderStyle.THIN);
		dataCellStyle.setBorderTop(BorderStyle.THIN);
		dataCellStyle.setBorderBottom(BorderStyle.THIN);
		
		Font font = wb.createFont();
		
		dataCellStyle.setFont(font);
		
		return dataCellStyle;
	}
	
	private void autoSizeColumns() {
		if (sheet.getPhysicalNumberOfRows() > 0) {
			SXSSFRow row = sheet.getRow(sheet.getFirstRowNum());
			Iterator<Cell> cellIterator = row.cellIterator();
			sheet.trackAllColumnsForAutoSizing(); // 모든 컬럼을 찾은 후 자동 사이즈 조절
			while (cellIterator.hasNext()) {
				int columnIndex = cellIterator.next().getColumnIndex();
				int currentColumnWidth = sheet.getColumnWidth(columnIndex);
				sheet.setColumnWidth(columnIndex, (currentColumnWidth + 2500));
			}
		}
	}
}
