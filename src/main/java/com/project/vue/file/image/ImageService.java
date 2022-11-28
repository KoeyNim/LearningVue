package com.project.vue.file.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	/* TEMP 이미지 경로 **/
	@Value("${site.temp-image}")
	private String TEMP_IMAGE_PATH;
	
	/* 이미지 업로드 경로 **/
	@Value("${site.image}")
	private String IMAGE_UPLOAD_PATH;

	public ImageEntity temp(MultipartFile image) {
		log.debug("image temp - image name : {}", image.getOriginalFilename());
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Path path = Path.of(TEMP_IMAGE_PATH);
		try {
			/* 상위 디렉토리까지 폴더 생성 **/
			if (path.toFile().mkdirs()) {
				log.debug("execute mkdirs - path {}", TEMP_IMAGE_PATH);
			}
			ImageEntity entity = new ImageEntity();
			entity.setFileNm(uuid);
			entity.setFileSize(image.getSize());
			entity.setFilePath(IMAGE_UPLOAD_PATH);
			entity.setContentType(image.getContentType());
			entity.setOrignFileNm(image.getOriginalFilename());

			image.transferTo(path.resolve(uuid));
			return entity;
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO Exception
		}
	}

	@Transactional
	public void save(ImageEntity entity) {
		Path tempPath = Path.of(TEMP_IMAGE_PATH).resolve(entity.getFileNm());
		Path savePath = Path.of(IMAGE_UPLOAD_PATH).resolve(entity.getFileNm());
			try {
				Files.copy(tempPath, savePath);
				Files.delete(tempPath);
			} catch (IOException e) {
				throw new RuntimeException(e); // TODO Exception
			}
		imageRepository.save(entity);
	}

	public ImageEntity findByFileNm(String fileNm) {
		return imageRepository.findByFileNm(fileNm).orElseThrow(RuntimeException::new); // TODO Exception
	}
}
