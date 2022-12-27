package com.project.vue.file.image;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

	Optional<ImageEntity> findByFileNm(String fileNm);

	void deleteByFileNm(String fileNm);

	List<ImageEntity> findAllByBoardSeqno(Long boardSeqno);
}
