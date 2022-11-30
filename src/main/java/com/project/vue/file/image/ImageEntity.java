package com.project.vue.file.image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "image")
public class ImageEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imageSeqno;
	
	private Long boardSeqno;

	@Column(columnDefinition = "varchar(100)")
	private String fileNm;

	private Long fileSize;

	@Column(columnDefinition = "varchar(100)")
	private String contentType;

	@Column(columnDefinition = "varchar(100)")
	private String orignFileNm;
}
