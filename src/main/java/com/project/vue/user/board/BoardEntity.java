package com.project.vue.user.board;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.hibernate.annotations.DynamicUpdate;

import com.project.vue.common.TimeEntity;
import com.project.vue.common.excel.annotation.ExcelColumnOptions;
import com.project.vue.common.excel.annotation.ExcelFileName;
import com.project.vue.common.file.FileEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "board")
@ExcelFileName(fileName = "게시판") // excel/annotation/ExcelFileName.java
@DynamicUpdate
@SequenceGenerator(
		name = "BOARD_SEQ_NO_01_GENERATOR",
		sequenceName = "BOARD_SEQ_NO_01",
		initialValue = 1, allocationSize = 1
)
public class BoardEntity extends TimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_NO_01_GENERATOR")
	@ExcelColumnOptions(headerName = "No", ColumnStyle = BorderStyle.DASH_DOT)
	/** 게시글 기본키 */
	private Long boardSeqno;

	@NotBlank
	@ExcelColumnOptions(headerName = "제목", ColumnStyle = BorderStyle.DOUBLE)
	/** 제목 */
	private String title;

	@NotBlank
	@Column(columnDefinition = "LONGTEXT")
	@ExcelColumnOptions(headerName = "내용", ColumnStyle = BorderStyle.MEDIUM_DASHED)
	/** 내용 */
	private String content;

	@NotBlank
	@Column(columnDefinition = "varchar(32)")
	@ExcelColumnOptions(headerName = "작성자", ColumnStyle = BorderStyle.THICK)
	/** 작성자 */
	private String userId;

	@Column(columnDefinition = "BIGINT default 0")
	@ExcelColumnOptions(headerName = "조회수", ColumnStyle = BorderStyle.DOTTED)
	/** 조회수 */
	private Integer count = 0;

	@OneToOne(cascade = CascadeType.REMOVE) // 게시글 삭제 시 파일 데이터도 같이 삭제
	@JoinColumn(name = "fileEntity") // referencedColumnName 미지정시 PK로 자동지정
	/** 첨부파일 */
	private FileEntity fileEntity;

	@Transient
	/** 해당 게시글에 접근한 유저아이디 */
	private String authUserId;
}