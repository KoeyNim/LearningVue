package com.project.vue.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.project.vue.common.excel.annotation.ExcelColumnOptions;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeEntity {
	
	@CreatedDate
	@ExcelColumnOptions(headerName = "작성일", ColumnStyle = BorderStyle.DASH_DOT)
	private String registDate;

	@LastModifiedDate
	@ExcelColumnOptions(headerName = "수정일", ColumnStyle = BorderStyle.DASH_DOT)
	private String modifyDate;
	
    @PrePersist
    public void onPrePersist() {
    	this.registDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    	this.modifyDate = this.registDate;
    }    
    
    @PreUpdate
    public void onPreUpdate(){
    	this.modifyDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}
