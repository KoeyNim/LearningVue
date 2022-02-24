package com.project.vue.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
public class FileController {
	
	private final FileService fileService;
	
	@PostMapping("fileupload")
	public ResponseEntity<FileEntity> upload(MultipartFile imgFile) throws Exception {
		log.debug("@@{}",imgFile);
		return ResponseEntity.ok(fileService.save(imgFile));

	}
	
	@GetMapping("download/{id}")
	public ResponseEntity<Resource> download(@PathVariable Long id) {
		try {
			FileEntity fileEntity = fileService.findById(id); // 파일 객체 찾기
			String path = fileEntity.getFilePath() + fileEntity.getFileNm();
			Path filePath = Paths.get(path); // 파일 Path 얻기
			Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
			
			File file = new File(path);
			String contentType = Files.probeContentType(filePath); // 타입 받아오기
			
			return ResponseEntity.ok()
					// attachement = 로컬에 저장, filename 파일 이름
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + file.getName() +"\"")
					.header(HttpHeaders.CONTENT_TYPE, contentType) 
					.body(resource);
		} catch(Exception e) {
			return new ResponseEntity<Resource>(HttpStatus.CONFLICT);
		}
	}
	
}
