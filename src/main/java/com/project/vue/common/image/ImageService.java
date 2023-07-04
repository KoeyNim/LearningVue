package com.project.vue.common.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
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
public class ImageService {

	private final ImageRepository imageRepository;

	/** TEMP 이미지 경로 */
	@Value("${site.temp-image}")
	private String TEMP_IMAGE_PATH;

	/** 이미지 업로드 경로 */
	@Value("${site.image}")
	private String IMAGE_UPLOAD_PATH;

	/**
	 * 이미지 temp 파일 생성
	 * @param img 이미지 객체
	 * @return String 이미지명
	 */
	public ImageTempResponse temp(MultipartFile img) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Path path = Paths.get(TEMP_IMAGE_PATH);
		try {
			/** 상위 디렉토리까지 폴더 생성 */
			if (path.toFile().mkdirs()) log.debug("execute mkdirs - path {}", TEMP_IMAGE_PATH);

			img.transferTo(path.resolve(uuid));

			ImageTempResponse res = ImageTempResponse.builder()
					.imgNm(uuid)
					.orignFileNm(img.getOriginalFilename())
					.contentType(img.getContentType()).build();
			return res;
		} catch (IOException ex) {
			throw new BizException("Image Temp File Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 이미지 경로
	 * @param imgNm 이미지명
	 * @return Path 이미지 경로
	 */
	public Path getPath(String imgNm) {
		Path path = Paths.get(TEMP_IMAGE_PATH + imgNm);
		/** TEMP 폴더에 파일 존재여부 체크 */
		if (!path.toFile().exists()) path = Paths.get(IMAGE_UPLOAD_PATH + imgNm);
		return path;
	}

	public void save(List<ImageTempResponse> imgList, Long boardSeqno) {
		List<ImageEntity> list = new ArrayList<>();

		Path tempPath = Paths.get(TEMP_IMAGE_PATH);
		Path savePath = Paths.get(IMAGE_UPLOAD_PATH);

		if (savePath.toFile().mkdirs()) log.debug("execute mkdirs - path {}", IMAGE_UPLOAD_PATH);

		for(var img : imgList) {
			try {
				Path tempImgPath = tempPath.resolve(img.getImgNm());
				Path saveImgPath = savePath.resolve(img.getImgNm());
				/* 동일 파일 존재시 덮어쓰기 **/
				Path path = Files.copy(tempImgPath, saveImgPath, StandardCopyOption.REPLACE_EXISTING);
				/* TEMP 폴더에 있는 파일 삭제 **/
				Files.delete(tempImgPath);

				ImageEntity entity = ImageEntity.builder()
						.boardSeqno(boardSeqno)
						.fileNm(img.getImgNm())
						.fileSize(Files.size(path))
						.contentType(img.getContentType())
						.orignFileNm(img.getOrignFileNm()).build();

				list.add(entity);
			} catch (IOException ex) {
				throw new BizException("Image Save Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
			}
		}
		imageRepository.saveAll(list);
	}

	public ImageEntity findByFileNm(String imgNm) {
		return imageRepository.findByFileNm(imgNm)
				.orElseThrow(() -> new BizException("Data is Not Found", ErrorCode.NOT_FOUND));
	}

	/**
	 * 이미지 삭제
	 * @param delImgList 이미지명 리스트
	 */
	@Transactional
	public void delete(List<String> delImgList) {
		log.debug("--- delete image");
		for (var delImg : delImgList) {
			Path filePath = Paths.get(IMAGE_UPLOAD_PATH).resolve(delImg);
			log.debug("--- filePath : {}", filePath.toString());
			File file = filePath.toFile();
			if(file.delete()) {
				log.debug("--- delete success image : {}", file.getName());
				imageRepository.deleteByFileNm(delImg);
			}
		}
	}

	/**
	 * 이미지 전체 삭제
	 * @param boardSeqno 게시글 키 번호
	 */
	public void deleteAll(long boardSeqno) {
		List<ImageEntity> imageEntities = imageRepository.findAllByBoardSeqno(boardSeqno);
		if(CollectionUtils.isEmpty(imageEntities)) {
			log.debug("--- list is empty");
			return;
		}
		List<String> delImgList = imageEntities.stream().map(ImageEntity::getFileNm).collect(Collectors.toList());
		delete(delImgList);
	}
}
