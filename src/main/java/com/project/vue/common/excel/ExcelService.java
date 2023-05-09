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

import com.project.vue.common.excel.annotation.ExcelFileName;
import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.BizException.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExcelService<T> {

	private SXSSFWorkbook wb; // 쓰기전용이며 읽기 불가능
	private List<ExcelDTO> resources;
	private SXSSFSheet sheet;
	private String excelNm;
	private List<T> excelData;
	private int rowNo;

	/**
	 * Constructor
	 * @param data 실 데이터 리스트
	 * @param cls 클래스 타입
	 */
	public ExcelService(List<T> data, Class<T> cls) {
		this.wb = new SXSSFWorkbook(100); // flush 범위 (기본값 100, -1 일 경우 제한 없음)
		this.resources = ExcelUtils.getResources(cls);
		this.excelData = data;
		this.excelNm = cls.getAnnotation(ExcelFileName.class).fileName();
	}

	/**
	 * 엑셀 생성
	 * @param os OutputStream
	 * @return String
	 * @throws IOException
	 */
	public String create(OutputStream os) throws IOException {
		rowNo = 0;
		sheet = wb.createSheet(excelNm);

		renderExcel(excelData, resources);

		wb.write(os);
		wb.dispose();
		wb.close();

		StringBuilder fileNm = new StringBuilder();
		fileNm.append(excelNm);
		fileNm.append("_");
		fileNm.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
		fileNm.append(FilenameUtils.EXTENSION_SEPARATOR + "xlsx");

		return fileNm.toString();
	}

	/**
	 * 엑셀 렌더링
	 * @param excelData 실 데이터 리스트
	 * @param resources 옵션 데이터 리스트
	 */
	private void renderExcel(List<T> excelData, List<ExcelDTO> resources) {
		SXSSFRow headerRow = sheet.createRow(rowNo++);
		List<String> colsNm = resources.stream()
				.map(ExcelDTO::getColNm).collect(Collectors.toList());
		List<Integer> colsWidth = resources.stream()
				.map(ExcelDTO::getColWidth).collect(Collectors.toList());
		List<CellStyle> dataCellStyles = resources.stream()
				.map(resource -> dataCellStyle(resource.getColStyle())).collect(Collectors.toList());

		int cellIdx = 0;
		/** header cell 세팅 */
		for (var resource : resources) {
			SXSSFCell headerCell = headerRow.createCell(cellIdx++, CellType.STRING);
			headerCell.setCellValue(resource.getHeaderNm());
			headerCell.setCellStyle(headerCellStyle());
		}

		/** data row, cell 세팅 */
		for (var data : excelData) {
			SXSSFRow dataRow = sheet.createRow(rowNo++);
			for ( int colIdx = 0 ; colIdx < resources.size(); colIdx++ ) {
				renderDataCell(dataRow, colIdx, data, colsNm, dataCellStyles);
				if (sheet.getPhysicalNumberOfRows() > 0) {
					sheet.setColumnWidth(colIdx, colsWidth.get(colIdx));
				}
			}
		}
	}

	/**
	 * 데이터 Cell 매핑
	 * @param dataRow dataRow
	 * @param colIdx column index
	 * @param data 실 데이터
	 * @param cols column 리스트
	 * @param cellStyles cell style 리스트
	 */
	private void renderDataCell(SXSSFRow dataRow, int colIdx, T data, List<String> colsNm, List<CellStyle> cellStyles) {
		SXSSFCell cell = dataRow.createCell(colIdx);
		try {
			// 해당하는 method를 찾음
			Method method = data.getClass().getMethod("get" + colsNm.get(colIdx));
			// method 실행
			Object methodValue = method.invoke(data);

			if (methodValue instanceof Integer) {
				cell.setCellValue((Integer)methodValue);
				cell.setCellStyle(cellStyles.get(colIdx));
				return;
			}

			if (methodValue instanceof Long) {
				cell.setCellValue((Long)methodValue);
				cell.setCellStyle(cellStyles.get(colIdx));
				return;
			}
			cell.setCellValue(ObjectUtils.isEmpty(methodValue) ? "" : methodValue.toString());
			cell.setCellStyle(cellStyles.get(colIdx));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new BizException("Excel Cell Mapping Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 헤더 Cell 스타일
	 * @return CellStyle
	 */
	private CellStyle headerCellStyle() {
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
	private CellStyle dataCellStyle(BorderStyle style) {
		CellStyle dataCellStyle = wb.createCellStyle();
		Font font = wb.createFont();

		dataCellStyle.setBorderLeft(style);
		dataCellStyle.setBorderRight(style);
		dataCellStyle.setBorderTop(style);
		dataCellStyle.setBorderBottom(style);

		dataCellStyle.setFont(font);

		return dataCellStyle;
	}
}
