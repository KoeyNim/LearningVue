package com.project.vue.common.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
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
import org.springframework.stereotype.Component;

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

	/**
	 *  엑셀 생성
	 */
	public String create(OutputStream os) throws IOException {
		rowNo = 0;
		sheet = wb.createSheet(resource.getExcelNm());

		renderHeaderRow(resource.getHeaderList());
		renderDataRow(dataList, resource.getColList(), resource.getColStyle());
		renderColimnSize(resource.getColList().size());

		wb.write(os);
		wb.dispose();
		wb.close();

		StringBuilder fileNm = new StringBuilder();
		fileNm.append(resource.getExcelNm());
		fileNm.append("_");
		fileNm.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
		fileNm.append(FilenameUtils.EXTENSION_SEPARATOR + "xlsx");

		return fileNm.toString();
	}

	/**
	 *  헤더 Row 생성
	 */
	private void renderHeaderRow(List<String> headerList) {
		SXSSFRow headerRow = sheet.createRow(rowNo++);
		int cellIdx = 0;
		for (String header : headerList) {
			SXSSFCell cell = headerRow.createCell(cellIdx++, CellType.STRING);
			cell.setCellValue(header);
			cell.setCellStyle(makeHeaderCellStyle());
		}
	}

	/**
	 * 데이터 Row 생성
	 */
	private void renderDataRow(List<T> dataList, List<String> colList, List<BorderStyle> colStyle) {
		List<CellStyle> cellStyleList = colStyle.stream()
				.map(cellStyle -> makeDataCellStyle(cellStyle)).collect(Collectors.toList());

		for (T data : dataList) {
			SXSSFRow row = sheet.createRow(rowNo++);
			for ( int columnIdx = 0 ; columnIdx < colList.size(); columnIdx++ ) {
				renderDataCell(row, columnIdx, data, colList, cellStyleList);
			}
		}
	}

	/**
	 * 데이터 Cell 매핑
	 * CellStyle 복제 (excel 2007 기준 64000개가 넘어갈 경우 에러 발생)
	 */
	private void renderDataCell(SXSSFRow row, int cellIdx, T data, List<String> colList, List<CellStyle> cellStyleList) {
		SXSSFCell cell = row.createCell(cellIdx);
		try {
			// 해당하는 method를 찾음
			Method method = data.getClass().getMethod("get" + colList.get(cellIdx));
			// method 실행
			Object methodValue = method.invoke(data);

			if (methodValue instanceof Integer) {
				cell.setCellValue((Integer)methodValue);
				cell.setCellStyle(cellStyleList.get(cellIdx));
				return;
			}

			if (methodValue instanceof Long) {
				cell.setCellValue((Long)methodValue);
				cell.setCellStyle(cellStyleList.get(cellIdx));
				return;
			}
			cell.setCellValue(ObjectUtils.isEmpty(methodValue) ? "" : methodValue.toString());
			cell.setCellStyle(cellStyleList.get(cellIdx));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e); // TODO exception..
		}
	}

	/** 
	 * 헤더 Cell 스타일
	 * @return CellStyle
	 */
	private CellStyle makeHeaderCellStyle() {
		CellStyle headerCellStyle = wb.createCellStyle();
		Font font = wb.createFont();

		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		headerCellStyle.setBorderRight(BorderStyle.MEDIUM);
		headerCellStyle.setBorderTop(BorderStyle.MEDIUM);
		headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);

		headerCellStyle.setFont(font);
		font.setBold(true);

		return headerCellStyle;
	}

	/**
	 * 데이터 Cell 스타일
	 * @param style 셀 테두리 스타일
	 * @return CellStyle
	 */
	private CellStyle makeDataCellStyle(BorderStyle style) {
		CellStyle dataCellStyle = wb.createCellStyle();
		Font font = wb.createFont();

		dataCellStyle.setBorderLeft(style);
		dataCellStyle.setBorderRight(style);
		dataCellStyle.setBorderTop(style);
		dataCellStyle.setBorderBottom(style);

		dataCellStyle.setFont(font);

		return dataCellStyle;
	}

	/**
	 * Column 사이즈 지정
	 * @param colListSize List 최대길이
	 */
	private void renderColimnSize(int colListSize) {
		if (sheet.getPhysicalNumberOfRows() > 0) {
			for (int i = 0 ; i <= colListSize ; i++) {
				int currentColumnWidth = sheet.getColumnWidth(i);
				sheet.setColumnWidth(i, (currentColumnWidth + 2500));
			}
		}
	}
}
