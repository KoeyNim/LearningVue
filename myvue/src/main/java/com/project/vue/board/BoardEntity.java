package com.project.vue.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
@Entity
@Table(name = "board")
public class BoardEntity {

	@Id
	@NotEmpty
	@NotBlank
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@NotBlank
	private String title;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "LONGTEXT")
	private String content;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "varchar(32)")
	private String userId;

	@NotEmpty
	@NotBlank
	@CreatedDate
	private Integer registDate;

	@NotEmpty
	@NotBlank
	@LastModifiedDate
	private Integer modifyDate;

	@NotEmpty
	@NotBlank
	private Integer count = 0;

	@NotEmpty
	@NotBlank
	private String fileId;

}
