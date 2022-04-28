package com.project.vue.file;

import java.io.File;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	private final FileRepository fileRepository;
	
	// apllication.yml 에서 경로 설정
	@Value("${site.upload}")
	private String FILE_UPLOAD_PATH;

	@Transactional
    public FileEntity save(MultipartFile imgFile) throws Exception {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//		String ext = "."+ StringUtils.substringAfter(imgFile.getOriginalFilename(), "."); // 파일 이름에서 확장자 추출
		FileEntity fileEntity = new FileEntity();
		String filePath = FILE_UPLOAD_PATH;
		File fileUpload = new File(filePath + uuid);
		
		fileEntity.setFileNm(uuid);
		fileEntity.setFileSize(imgFile.getSize());
		fileEntity.setFilePath(filePath);
		fileEntity.setContentType(imgFile.getContentType());
		fileEntity.setOrignFileNm(imgFile.getOriginalFilename());
		
		imgFile.transferTo(fileUpload);
		fileRepository.save(fileEntity);
		return fileEntity;
    }
	
	public FileEntity findById(Long id) {
		return fileRepository.findById(id).orElseThrow();
	}

	
}