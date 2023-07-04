package com.project.vue.common.file;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
	
	private final FileRepository fileRepository;
	
	/** 파일 업로드 경로 */
	@Value("${site.upload}")
	private String FILE_UPLOAD_PATH;

	/**
	 * 파일 업로드
	 * @param file MultipartFile
	 * @return FileEntity
	 */
    public FileEntity upld(MultipartFile file) {
		log.debug("file upload - file name : {}", file.getOriginalFilename());
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Path path = Paths.get(FILE_UPLOAD_PATH);
		try {
			/** 상위 디렉토리까지 폴더 생성 */
			if (path.toFile().mkdirs()) log.debug("execute mkdirs - path {}", FILE_UPLOAD_PATH);

			FileEntity entity = FileEntity.builder()
					.fileNm(uuid)
					.fileSize(file.getSize())
					.contentType(file.getContentType())
					.orignFileNm(file.getOriginalFilename()).build();

			file.transferTo(path.resolve(uuid));
			fileRepository.save(entity);
			return entity;
		} catch (IOException ex) {
			throw new BizException("File Upload Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
    }

	/**
	 * 파일 다운로드
	 * @param id file 기본키
	 * @param os OutputStream
	 * @return String
	 */
	public String dwld(long id, OutputStream os) {
		FileEntity entity = fileRepository.findById(id)
				.orElseThrow(() -> new BizException("File Data is Not Found", ErrorCode.NOT_FOUND));
		Path path = Paths.get(FILE_UPLOAD_PATH).resolve(entity.getFileNm());
		try {
			os.write(Files.readAllBytes(path));
			return entity.getOrignFileNm();
		} catch (IOException ex) {
			throw new BizException("File Download Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 파일 삭제
	 * @param entity FileEntity
	 */
	public void delete(FileEntity entity) {
		log.debug("--- delete file");
		Path filePath = Paths.get(FILE_UPLOAD_PATH).resolve(entity.getFileNm());
		log.debug("--- path : {}", filePath.toString());
		File file = filePath.toFile();
		if(file.delete()) log.debug("--- delete success file : {}", file.getName());
	}
}