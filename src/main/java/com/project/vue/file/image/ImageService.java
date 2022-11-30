package com.project.vue.file.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	private static final ObjectMapper OM = new ObjectMapper();

	/* TEMP 이미지 경로 **/
	@Value("${site.temp-image}")
	private String TEMP_IMAGE_PATH;

	/* 이미지 업로드 경로 **/
	@Value("${site.image}")
	private String IMAGE_UPLOAD_PATH;

	/**
	 * 이미지 temp 파일 생성
	 * @param image 이미지 객체
	 * @return String 이미지명
	 */
	public String temp(MultipartFile image) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Path path = Paths.get(TEMP_IMAGE_PATH);
		try {
			/* 상위 디렉토리까지 폴더 생성 **/
			if (path.toFile().mkdirs()) {
				log.debug("execute mkdirs - path {}", TEMP_IMAGE_PATH);
			}
			image.transferTo(path.resolve(uuid));
			return uuid;
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO Exception
		}
	}
	
	/**
	 * 이미지 파일 경로
	 * @param imageNm 이미지명
	 * @return Path 이미지 경로
	 */
	public Path getPath(String imageNm) {
		Path path = Paths.get(TEMP_IMAGE_PATH + imageNm);
		/* TEMP 폴더에 파일이 존재하는지 체크 **/
		if (!path.toFile().exists()) {
			path = Paths.get(IMAGE_UPLOAD_PATH + imageNm);
		}
		return path;
	}
	
	@Transactional
	public void save(String json, long boardSeqno) {
		try {
			/* JSON String DeSerialize **/
			JsonNode jn = OM.readTree(json);
			ImageEntity entity = OM.treeToValue(jn, ImageEntity.class);
			entity.setBoardSeqno(boardSeqno);
			
			Path tempPath = Paths.get(TEMP_IMAGE_PATH).resolve(entity.getFileNm());
			Path savePath = Paths.get(IMAGE_UPLOAD_PATH).resolve(entity.getFileNm());
			Files.copy(tempPath, savePath);
			Files.delete(tempPath);
			imageRepository.save(entity);
		} catch (IllegalArgumentException | IOException e) {
			throw new RuntimeException(e); // TODO Exception
		}
	}

	public ImageEntity findByFileNm(String fileNm) {
		return imageRepository.findByFileNm(fileNm).orElseThrow(RuntimeException::new); // TODO Exception
	}
	
	/**
	 * 이미지 전체 삭제
	 * @param boardSeqno
	 */
	public void delete(long boardSeqno) {
		imageRepository.deleteAllByBoardSeqno(boardSeqno);
	}
}
