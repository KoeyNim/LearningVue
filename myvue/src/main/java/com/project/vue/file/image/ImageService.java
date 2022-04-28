package com.project.vue.file.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Value("${site.image}")
	private String IMAGE_UPLOAD_PATH;
	
	@Transactional
	public ImageEntity save(MultipartFile image) throws Exception {
		try {
			if(image.isEmpty()) {
				throw new Exception("Failed to store empty image " + image.getOriginalFilename());
			}
			
			String fileNm = image(image);
			ImageEntity imageEntity = new ImageEntity();
			
			imageEntity.setOrignFileNm(image.getOriginalFilename());
			imageEntity.setFileNm(fileNm);
			imageEntity.setContentType(image.getContentType());
			imageEntity.setFileSize(image.getResource().contentLength());
			imageEntity.setFilePath(IMAGE_UPLOAD_PATH);   
			
			imageRepository.save(imageEntity);
			return imageEntity;
			
		} catch (Exception e) {
			throw new Exception("Failed to store file " + image.getOriginalFilename(), e);
		}
	}

	public ImageEntity findById(Long id) {
		return imageRepository.findById(id).orElseThrow();
	}
	
	// 이미지 체크 후 이름 생성
	public String image(MultipartFile image) throws IOException {
		File uploadDir = new File(IMAGE_UPLOAD_PATH);
		
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String ext = "."+ StringUtils.substringAfter(image.getOriginalFilename(), ".");
		String fileNm = uuid + ext;
		File saveFile = new File(IMAGE_UPLOAD_PATH, fileNm);
		FileCopyUtils.copy(image.getBytes(), saveFile);
		
		return fileNm;
	}
}
