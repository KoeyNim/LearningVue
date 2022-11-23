package com.project.vue.file;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
	
	private final FileRepository fileRepository;
	
	/* application.yml file 경로 **/
	@Value("${site.upload}")
	private String FILE_UPLOAD_PATH;

	@Transactional // TODO Path 사용 및 void로 변경
    public FileEntity upld(MultipartFile file) throws Exception {
		log.debug("file upload - file name : {}", file.getOriginalFilename());
		try {
			if(file.isEmpty()) {
				throw new Exception("Failed to store empty image " + file.getOriginalFilename());
			}
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			File uploadDir = new File(FILE_UPLOAD_PATH + uuid);
			
			Path path = Path.of(FILE_UPLOAD_PATH + uuid);
			
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
	
	/**
	 * @param id file 키값
	 * @param os outputStream
	 * @return <String> 파일명
	 */
	public String dwld(long id, OutputStream os) {
		FileEntity entity = fileRepository.findById(id).orElseThrow(RuntimeException::new); // TODO Exception
		Path filePath = Paths.get(entity.getFilePath() + entity.getFileNm());
		try {
			os.write(Files.readAllBytes(filePath));
			return entity.getOrignFileNm();
		} catch (IOException e) { // TODO Exception
			throw new RuntimeException(e);
		}
	}
}