package com.project.vue.common.image;

import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.vue.common.Constants;
import com.project.vue.common.PathConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/image")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	/**
	 * Temp 이미지 파일 생성
	 * @param img MultipartFile
	 * @return ImageTempResponse
	 */
	@PostMapping("temp")
	public ImageTempResponse temp(MultipartFile img) {
		log.debug("api/v1/image/temp - posts - img : {}", img.getOriginalFilename());
		return imageService.temp(img);
	}

	/**
	 * 에디터에 미리보기로 보여줄 이미지 파일을 해당하는 경로에서 찾아 Resource로 반환
	 * @param imgNm 이미지 파일명
	 * @return Resource
	 */
	@GetMapping("find/{imgNm}")
	public Resource find(@PathVariable String imgNm) {
		log.debug("api/v1/image/find - gets - imgNm : {}", imgNm);
		return new FileSystemResource(imageService.getPath(imgNm));
	}
	
	/**
	 * 파일 단일 및 다중 삭제
	 * @param delImgList 이미지 파일명 리스트
	 */
	@DeleteMapping(PathConstants.DELETE)
	public void delete(@RequestParam(value = "delImgList[]") List<String> delImgList) {
		log.debug("api/v1/image/delete - delete - delImgList : {}", delImgList);
		imageService.delete(delImgList);
	}
}
