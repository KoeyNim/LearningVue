package com.project.vue.file.image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.project.vue.common.TimeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "image")
public class ImageEntity extends TimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "varchar(100)")
	private String fileNm;
	
	private Long fileSize;
	
	@Column(columnDefinition = "varchar(100)")
	private String filePath;
	
	@Column(columnDefinition = "varchar(100)")
	private String contentType;

	@Column(columnDefinition = "varchar(100)")
	private String orignFileNm;


}
