package com.project.vue.common.excel.service;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
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
import com.project.vue.common.excel.DTO.ExcelDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExcelService<T> {

	private SXSSFWorkbook wb; // 쓰기전용이며 읽기 불가능
	private ExcelDTO resource;
	private SXSSFSheet sheet;
	private List<T> dataList;
	private int rowNo;
	/**
	 *  생성자
	 */
	public ExcelService(List<T> dataList, Class<T> type) {
		this.wb = new SXSSFWorkbook(100); // flush 범위 (기본값 100, -1 일 경우 제한 없음)
		this.resource = ExcelUtils.getResource(type);
		this.dataList = dataList;
	}

	public ResponseEntity<ByteArrayResource> downloadExcel() throws Exception {
		try {
			rowNo = 0;

			sheet = wb.createSheet(resource.getFileName());

			renderHeaderRow(resource.getHeaderList());
			renderDataRow(dataList, resource.getColList(), resource.getColStyle());
			renderColimnSize(resource.getColList().size());

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			wb.write(stream);
			wb.dispose();
			wb.close();
	    	String fileName = resource.getFileName()+"_"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))+".xlsx";
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
	private void renderDataRow(List<T> dataList, List<String> colList, List<BorderStyle> colStyle) {
		List<CellStyle> cellStyleList = new ArrayList<>();

		for ( int dataIdx = 0 ; dataIdx < dataList.size() ; dataIdx++ ) {
			SXSSFRow dataRow = sheet.createRow(rowNo++);
			if (dataIdx == 0) { // CellStyle 최초 한번만 생성
				for ( int columnStyleIdx = 0 ; columnStyleIdx < colList.size() ; columnStyleIdx++ ) {
					cellStyleList.add(makeDataCellStyle(colStyle.get(columnStyleIdx)));
				}
			}
			for ( int columnIdx = 0 ; columnIdx < colList.size() ; columnIdx++ ) {
				renderDataCell(dataRow, columnIdx, dataList.get(dataIdx), colList.get(columnIdx), cellStyleList.get(columnIdx));
			}
		}
	}

	/**
	 * 데이터 Cell 매핑
	 * CellStyle 복제 (excel 2007 기준 64000개가 넘어갈 경우 에러 발생)
	 */
	private void renderDataCell(SXSSFRow dataRow, int cellIdx, T data, String colList, CellStyle style)
	{
		try {
			// 해당하는 method를 찾음
			SXSSFCell cell = dataRow.createCell(cellIdx);
			Method method = data.getClass().getMethod("get" + colList);
			// method 실행
			Object methodValue = method.invoke(data);

			if (java.lang.Integer.class.isInstance(methodValue)) {
				cell.setCellValue((Integer)methodValue);
				cell.setCellStyle(style);
				return;
			}
			if (java.lang.Long.class.isInstance(methodValue)) {
				cell.setCellValue((Long)methodValue);
				cell.setCellStyle(style);
				return;
			}
			if (java.time.LocalDate.class.isInstance(methodValue)) {
				cell.setCellValue((LocalDate)methodValue);
				cell.setCellStyle(style);
				return;
			}
			cell.setCellValue(ObjectUtils.isEmpty(methodValue) ? "" : methodValue.toString());
			cell.setCellStyle(style);

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
	private CellStyle makeDataCellStyle(BorderStyle style)
	{
		CellStyle dataCellStyle = wb.createCellStyle();

		dataCellStyle.setBorderLeft(style);
		dataCellStyle.setBorderRight(style);
		dataCellStyle.setBorderTop(style);
		dataCellStyle.setBorderBottom(style);

		Font font = wb.createFont();

		dataCellStyle.setFont(font);

		return dataCellStyle;
	}

	/**
	 * Column 사이즈 조절
	 */
	private void renderColimnSize(int colLength) {
		if (sheet.getPhysicalNumberOfRows() > 0) {
			for (int i = 0 ; i <= colLength ; i++) {
				int currentColumnWidth = sheet.getColumnWidth(i);
				sheet.setColumnWidth(i, (currentColumnWidth + 2500));
			}
		}
	}

	// flush 제한을 걸 경우 SXSSFRow는 쓰기 전용이라 getRow 메소드를 사용할 수 없으므로 null값이 나옴.
//	private void autoSizeColumns() {
//		if (sheet.getPhysicalNumberOfRows() > 0) {
//			SXSSFRow row = sheet.getRow(sheet.getFirstRowNum());
//			Iterator<Cell> cellIterator = row.cellIterator();
//			sheet.trackAllColumnsForAutoSizing(); // 모든 컬럼을 찾은 후 자동 사이즈 조절 (속도저하)
//			while (cellIterator.hasNext()) {
//				int columnIndex = cellIterator.next().getColumnIndex();
//				int currentColumnWidth = sheet.getColumnWidth(columnIndex);
//				sheet.setColumnWidth(columnIndex, (currentColumnWidth + 2500));
//			}
//		}
//	}
}
