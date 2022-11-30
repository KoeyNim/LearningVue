package com.project.vue.file;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "file")
public class FileEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileSeqno;
	
	@Column(columnDefinition = "varchar(100)")
	private String fileNm;
	
	private Long fileSize;
	
	@Column(columnDefinition = "varchar(100)")
	private String contentType;

	@Column(columnDefinition = "varchar(100)")
	private String orignFileNm;


}
