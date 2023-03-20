package com.project.vue.common.image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "image")
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	/** 이미지 기본키 */
	private Long imageSeqno;

	/** 게시글 기본키 */
	private Long boardSeqno;

	@Column(columnDefinition = "varchar(100)")
	/** 이미지명 */
	private String fileNm;

	/** 이미지 크기 */
	private Long fileSize;

	@Column(columnDefinition = "varchar(100)")
	/** 이미지 타입 */
	private String contentType;

	@Column(columnDefinition = "varchar(100)")
	/** 실제 이미지명 */
	private String orignFileNm;
}
