package com.project.vue.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.project.vue.common.BaseTimeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "board")
public class BoardEntity extends BaseTimeEntity {

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

	@Column(columnDefinition = "BIGINT" )
	private Integer count = 0;

	private String fileId;

}
