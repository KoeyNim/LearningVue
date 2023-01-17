package com.project.vue.file.image;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImageTempResponse {

	/** 파일명 */
	private String imgNm;
	/** 실제 파일명 */
	private String orignFileNm;
	/** 파일 타입 */
	private String contentType;
}
