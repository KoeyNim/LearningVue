package com.project.vue.board;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.apache.poi.ss.usermodel.BorderStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.vue.common.TimeEntity;
import com.project.vue.common.excel.annotation.ExcelColumnOptions;
import com.project.vue.common.excel.annotation.ExcelFileName;
import com.project.vue.file.FileEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "board")
@ExcelFileName(fileName = "게시판") // excel/annotation/ExcelFileName.java
public class BoardEntity extends TimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ExcelColumnOptions(headerName = "No", ColumnStyle = BorderStyle.DASH_DOT)
	private Long id;

	@NotEmpty
	@NotBlank
	@ExcelColumnOptions(headerName = "제목", ColumnStyle = BorderStyle.DOUBLE)
	private String title;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "LONGTEXT")
	@ExcelColumnOptions(headerName = "내용", ColumnStyle = BorderStyle.MEDIUM_DASHED)
	private String content;

	@Column(columnDefinition = "varchar(32)")
	@ExcelColumnOptions(headerName = "작성자", ColumnStyle = BorderStyle.THICK)
	private String userId;

	@Column(columnDefinition = "BIGINT default 0")
	@ExcelColumnOptions(headerName = "조회수", ColumnStyle = BorderStyle.DOTTED)
	private Integer count = 0;

	@OneToOne(cascade = CascadeType.REMOVE) // 게시글 삭제 시 파일 데이터도 같이 삭제
	@JoinColumn (name = "fileId")
	@JsonIgnore // Cannot call sendError() after the response has been committed 에러 방지
	            // 직렬화 역직렬화에 사용되는 논리적 프로퍼티 값을 무시
	private FileEntity fileEntity;
	
	@Transient
	private Object authUserId;

}
