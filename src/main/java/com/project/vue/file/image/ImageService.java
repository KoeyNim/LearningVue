package com.project.vue.file.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	 * @param img 이미지 객체
	 * @return String 이미지명
	 */
	public String temp(MultipartFile img) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Path path = Paths.get(TEMP_IMAGE_PATH);
		try {
			/* 상위 디렉토리까지 폴더 생성 **/
			if (path.toFile().mkdirs()) {
				log.debug("execute mkdirs - path {}", TEMP_IMAGE_PATH);
			}
			img.transferTo(path.resolve(uuid));
			return uuid; // TODO 반환시 파일 정보를 반환하거나 저장된 파일로 orign 파일명, 파일 MINE TYPE 알아내는 방법 찾기.
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO Exception
		}
	}
	
	/**
	 * 이미지 경로
	 * @param imgNm 이미지명
	 * @return Path 이미지 경로
	 */
	public Path getPath(String imgNm) {
		Path path = Paths.get(TEMP_IMAGE_PATH + imgNm);
		/* TEMP 폴더에 파일 존재여부 체크 **/
		if (!path.toFile().exists()) {
			path = Paths.get(IMAGE_UPLOAD_PATH + imgNm);
		}
		return path;
	}
	
	@Transactional
	public void save(List<String> imgNmList, long boardSeqno) {
		List<ImageEntity> list = new ArrayList<>();

		imgNmList.forEach(el -> {
			try {
				Path tempPath = Paths.get(TEMP_IMAGE_PATH).resolve(el);
				Path savePath = Paths.get(IMAGE_UPLOAD_PATH).resolve(el);
				/* 동일 파일 존재시 덮어쓰기 **/
				Path path = Files.copy(tempPath, savePath, StandardCopyOption.REPLACE_EXISTING);
				/* TEMP 폴더에 있는 파일 삭제 **/
				Files.delete(tempPath);

				ImageEntity entity = new ImageEntity();
				entity.setBoardSeqno(boardSeqno);
				entity.setFileNm(el);
				entity.setFileSize(Files.size(path));
//				entity.setContentType(Files.(path)); 이미지 파일의 확장자가 없어 tika 라이브러리를 사용해야함.
//				entity.setOrignFileNm(Files.); 파일명이 변경되어 와서 알수없음.

				list.add(entity);
			} catch (IllegalArgumentException | IOException e) {
				throw new RuntimeException(e); // TODO Exception
			}
		});
		imageRepository.saveAll(list);
	}

	public ImageEntity findByFileNm(String imgNm) {
		return imageRepository.findByFileNm(imgNm).orElseThrow(RuntimeException::new); // TODO Exception
	}

	/**
	 * 이미지 삭제
	 * @param imageNm 이미지명
	 */
	@Transactional
	public void delete(List<String> imageNm) {
		log.debug("--- delete image");
		imageNm.forEach(el -> {
			Path filePath = Paths.get(IMAGE_UPLOAD_PATH).resolve(el);
			String fileNm = filePath.toFile().getName();
			log.debug("--- path : {}", filePath.toString());
			if(filePath.toFile().delete()) {
				log.debug("--- delete success image : {}", fileNm);
				imageRepository.deleteByFileNm(el);
			}
		});
	}

	/**
	 * 이미지 전체 삭제
	 * @param boardSeqno 게시글 키 번호
	 */
	@Transactional //TODO 전체 삭제시 delete method를 사용해서 실제 파일 삭제할것
	public void deleteAll(long boardSeqno) {
		imageRepository.deleteAllByBoardSeqno(boardSeqno);
	}
}
