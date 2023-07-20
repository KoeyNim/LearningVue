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
import com.project.vue.common.excel.annotation.ExcelFileName;
import com.project.vue.common.excel.annotation.ExcelOptions;
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
	@ExcelOptions(headerName = "No", columnStyle = BorderStyle.DASH_DOT, columnWidth = 10000)
	/** 게시글 기본키 */
	private Long boardSeqno;

	@NotBlank
	@ExcelOptions(headerName = "제목", columnStyle = BorderStyle.DOUBLE, columnWidth = 10000)
	/** 제목 */
	private String title;

	@NotBlank
	@Column(columnDefinition = "LONGTEXT")
	@ExcelOptions(headerName = "내용", columnStyle = BorderStyle.MEDIUM_DASHED, columnWidth = 10000)
	/** 내용 */
	private String content;

	@NotBlank
	@Column(columnDefinition = "varchar(32)")
	@ExcelOptions(headerName = "작성자", columnStyle = BorderStyle.THICK, columnWidth = 5000)
	/** 작성자 */
	private String userId;

	@Column(columnDefinition = "BIGINT default 0")
	@ExcelOptions(headerName = "조회수", columnStyle = BorderStyle.DOTTED, columnWidth = 5000)
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
