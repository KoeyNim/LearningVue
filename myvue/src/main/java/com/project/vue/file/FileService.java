package com.project.vue.file;

import java.io.File;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
	
	
	private final FileRepository fileRepository;


	@Transactional
    public FileEntity save(MultipartFile imgFile) throws Exception {
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		FileEntity file = new FileEntity();
		
		String filePath = "D:/W/bin/temp/upload/" + imgFile.getOriginalFilename();
		File fileUpload = new File(filePath);
		imgFile.transferTo(fileUpload);
		
		file.setFileNm(uuid+'_'+imgFile.getName());
		file.setFilePath(filePath);
		file.setFileSize(imgFile.getSize());
		file.setOrignFileNm(imgFile.getOriginalFilename());
		fileRepository.save(file);
		return file;
    }

	
}