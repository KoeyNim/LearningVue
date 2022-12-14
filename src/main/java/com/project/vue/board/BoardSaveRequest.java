package com.project.vue.board;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardSaveRequest {

	/** 제목 */
	private String title;
	/** 내용 */
	private String content;
	/** 파일 */
	private MultipartFile file;
	/** 이미지 파일명 리스트 */
	private List<String> imgNmList;
}
