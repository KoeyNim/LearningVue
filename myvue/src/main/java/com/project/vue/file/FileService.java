package com.project.vue.file;

import java.io.File;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.project.vue.common.Constants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	private final FileRepository fileRepository;

	@Transactional
    public FileEntity save(MultipartFile imgFile) throws Exception {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String ext = "."+ StringUtils.substringAfter(imgFile.getOriginalFilename(), ".");
		String fileNm = uuid + ext;
		FileEntity file = new FileEntity();
		String filePath = Constants.FILE_UPLOAD_PATH;
		File fileUpload = new File(filePath + fileNm);
		
		file.setFileNm(fileNm);
		file.setFileSize(imgFile.getSize());
		file.setFilePath(filePath);
		file.setContentType(imgFile.getContentType());
		file.setOrignFileNm(imgFile.getOriginalFilename());
		
		imgFile.transferTo(fileUpload);
		fileRepository.save(file);
		return file;
    }
	
	public FileEntity findById(Long id) {
		return fileRepository.findById(id).orElseThrow();
	}

	
}