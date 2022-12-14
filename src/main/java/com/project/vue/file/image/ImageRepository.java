package com.project.vue.file.image;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

	Optional<ImageEntity> findByFileNm(String fileNm);

	void deleteByFileNm(String fileNm);

	void deleteAllByBoardSeqno(long boardSeqno);
}
