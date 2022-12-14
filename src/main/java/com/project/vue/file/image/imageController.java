package com.project.vue.file.image;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/image")
@RequiredArgsConstructor
public class imageController {

	private final ImageService imageService;

	@PostMapping("temp")
	public String temp(MultipartFile img) {
		log.debug("api/v1/image/temp - posts - img : {}", img.getOriginalFilename());
		return imageService.temp(img);
	}

	@GetMapping("find/{imgNm}")
	public Resource find(@PathVariable String imgNm) {
		log.debug("api/v1/image/find - gets - imgNm : {}", imgNm);
		return new FileSystemResource(imageService.getPath(imgNm));
	}
	
	@DeleteMapping("delete")
	public void delete(@RequestParam(value = "delImgList[]") List<String> delImgList) {
		log.debug("api/v1/image/delete - delete - delImgList : {}", delImgList);
		imageService.delete(delImgList);
	}
}
