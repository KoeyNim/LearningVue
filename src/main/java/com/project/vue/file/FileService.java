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
    public FileEntity save(MultipartFile file) throws Exception {
		try {
			if(file.isEmpty()) {
				throw new Exception("Failed to store empty image " + file.getOriginalFilename());
			}
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			File uploadDir = new File(FILE_UPLOAD_PATH + uuid);
			
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			
	//		String ext = "."+ StringUtils.substringAfter(imgFile.getOriginalFilename(), "."); // 파일 이름에서 확장자 추출
			FileEntity fileEntity = new FileEntity();
			
			fileEntity.setFileNm(uuid);
			fileEntity.setFileSize(file.getSize());
			fileEntity.setFilePath(FILE_UPLOAD_PATH);
			fileEntity.setContentType(file.getContentType());
			fileEntity.setOrignFileNm(file.getOriginalFilename());
			
			file.transferTo(uploadDir);
			fileRepository.save(fileEntity);
		return fileEntity;
		} catch (Exception e) {
			throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
		}
    }
	
	public FileEntity findById(Long id) {
		return fileRepository.findById(id).orElseThrow();
	}
}