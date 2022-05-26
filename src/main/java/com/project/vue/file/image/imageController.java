package com.project.vue.file.image;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.vue.common.Constants;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
public class imageController {
	
	private final ImageService imageService;
	
	private final ResourceLoader resourceLoader;
	
	@PostMapping("imageupload")
	public ResponseEntity<String> upload(@RequestParam("image") MultipartFile image) throws Exception {
		ImageEntity imageEntity = imageService.save(image);
		return ResponseEntity.ok(Constants.REQUEST_MAPPING_PREFIX + "/image/" + imageEntity.getId());
	}
	
	@GetMapping("image/{id}")
	public ResponseEntity<Resource> findImage(@PathVariable Long id) throws Exception {
		ImageEntity imageEntity = imageService.findById(id);
		Resource resource = resourceLoader.getResource("file:" + imageEntity.getFilePath() + imageEntity.getFileNm());
		return ResponseEntity.ok(resource);
	}
}
