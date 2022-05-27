package com.project.vue.admin.post;

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
import com.project.vue.file.FileEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "board")
public class AdminPostEntity extends TimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@NotBlank
	private String title;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "LONGTEXT")
	private String content;

	@Column(columnDefinition = "varchar(32)")
	private String userId;

	@Column(columnDefinition = "BIGINT")
	private Integer count = 0;

	@OneToOne(cascade = CascadeType.REMOVE) // 게시글 삭제 시 파일 데이터도 같이 삭제
	@JoinColumn (name = "fileId")
	private FileEntity fileEntity;
	
	@Transient
	private Object authUserId;

}
