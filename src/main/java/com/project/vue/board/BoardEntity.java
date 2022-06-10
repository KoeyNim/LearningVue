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

import com.project.vue.common.TimeEntity;
import com.project.vue.common.excel.annotation.ExcelColumnName;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ExcelColumnName(headerName = "No")
	private Long id;

	@NotEmpty
	@NotBlank
	@ExcelColumnName(headerName = "제목")
	private String title;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "LONGTEXT")
	@ExcelColumnName(headerName = "내용")
	private String content;

	@Column(columnDefinition = "varchar(32)")
	@ExcelColumnName(headerName = "작성자")
	private String userId;

	@Column(columnDefinition = "BIGINT default 0")
	@ExcelColumnName(headerName = "조회수")
	private Integer count = 0;

	@OneToOne(cascade = CascadeType.REMOVE) // 게시글 삭제 시 파일 데이터도 같이 삭제
	@JoinColumn (name = "fileId")
	private FileEntity fileEntity;
	
	@Transient
	private Object authUserId;

}
