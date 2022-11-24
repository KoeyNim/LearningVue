package com.project.vue.file;

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

	/**
	 * 파일 업로드
	 * @param file 파일
	 * @return <FileEntity> entity
	 */
	@Transactional
    public FileEntity upld(MultipartFile file) {
		log.debug("file upload - file name : {}", file.getOriginalFilename());
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Path path = Path.of(FILE_UPLOAD_PATH);
		try {
			/* 상위 디렉토리까지 폴더 생성 **/
			if (path.toFile().mkdirs()) {
				log.debug("execute mkdirs - path {}", FILE_UPLOAD_PATH);
			}
			FileEntity entity = new FileEntity();
			entity.setFileNm(uuid);
			entity.setFileSize(file.getSize());
			entity.setFilePath(FILE_UPLOAD_PATH);
			entity.setContentType(file.getContentType());
			entity.setOrignFileNm(file.getOriginalFilename());

			file.transferTo(path.resolve(uuid));
			fileRepository.save(entity);
			return entity;
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO Exception
		}
    }
	
	/**
	 * 파일 다운로드
	 * @param id file 키값
	 * @param os outputStream
	 * @return <String> 파일명
	 */
	public String dwld(long id, OutputStream os) {
		FileEntity entity = fileRepository.findById(id).orElseThrow(RuntimeException::new); // TODO Exception
		Path path = Paths.get(entity.getFilePath() + entity.getFileNm());
		try {
			os.write(Files.readAllBytes(path));
			return entity.getOrignFileNm();
		} catch (IOException e) { // TODO Exception
			throw new RuntimeException(e);
		}
	}
}