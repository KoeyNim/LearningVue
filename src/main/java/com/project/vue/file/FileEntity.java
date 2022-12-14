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
	/* 파일 기본키 **/
	private Long fileSeqno;

	@Column(columnDefinition = "varchar(100)")
	/* 파일명 **/
	private String fileNm;

	/* 파일 크기 **/
	private Long fileSize;

	@Column(columnDefinition = "varchar(100)")
	/* 파일 타입 **/
	private String contentType;

	@Column(columnDefinition = "varchar(100)")
	/* 실제 파일명 **/
	private String orignFileNm;


}
