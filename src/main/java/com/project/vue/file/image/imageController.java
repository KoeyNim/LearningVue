package com.project.vue.file.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.vue.common.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/image")
@RequiredArgsConstructor
public class imageController {

	/* TEMP 이미지 경로 **/
	@Value("${site.temp-image}")
	private String TEMP_IMAGE_PATH;

	private final ImageService imageService;
	private final ResourceLoader resourceLoader;

	@PostMapping("temp")
	public ImageEntity temp(MultipartFile image) {
		log.debug("api/v1/image/temp - posts - image : {}", image.getOriginalFilename());
		return imageService.temp(image);
	}

	@GetMapping("find/{imageNm}")
	public Resource find(@PathVariable String imageNm) {
		log.debug("api/v1/image/find - gets - imageNm : {}", imageNm);
		Resource resource = resourceLoader.getResource("file:" + TEMP_IMAGE_PATH + imageNm);
		
		/* TEMP 폴더에 파일이 존재하는지 체크 **/
		if (!resource.exists()) {
			ImageEntity entity = imageService.findByFileNm(imageNm);
			resource = resourceLoader.getResource("file:" + entity.getFilePath() + entity.getFileNm());
		}

		return resource;
	}
}
