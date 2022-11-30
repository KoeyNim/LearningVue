package com.project.vue.file.image;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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

	private final ImageService imageService;

	@PostMapping("temp")
	public String temp(MultipartFile image) {
		log.debug("api/v1/image/temp - posts - image : {}", image.getOriginalFilename());
		return imageService.temp(image);
	}

	@GetMapping("find/{imageNm}")
	public Resource find(@PathVariable String imageNm) {
		log.debug("api/v1/image/find - gets - imageNm : {}", imageNm);
		return new FileSystemResource(imageService.getPath(imageNm));
	}
}
