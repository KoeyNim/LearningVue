package com.project.vue.admin.board;

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
import javax.validation.constraints.NotBlank;

import com.project.vue.common.TimeEntity;
import com.project.vue.common.file.FileEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "board")
@SequenceGenerator(
		name = "BOARD_SEQ_NO_01_GENERATOR",
		sequenceName = "BOARD_SEQ_NO_01",
		initialValue = 1, allocationSize = 1
)
public class AdminBoardEntity extends TimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_NO_01_GENERATOR")
	@Column(name = "boardSeqno")
	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	@Column(columnDefinition = "LONGTEXT")
	private String content;

	@Column(columnDefinition = "varchar(32)")
	private String userId;

	@Column(columnDefinition = "BIGINT default 0")
	private Integer count = 0;

	@OneToOne(cascade = CascadeType.REMOVE) // 게시글 삭제 시 파일 데이터도 같이 삭제
	@JoinColumn (name = "fileEntity") // referencedColumnName 미지정시 기본값 id
	private FileEntity fileEntity;
}
