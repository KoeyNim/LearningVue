package com.project.vue.admin.board;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.vue.admin.AdminSearchSpecification;
import com.project.vue.admin.payload.AdminBoardRequest;
import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.BizException.ErrorCode;
import com.project.vue.common.file.FileRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardService {

	private final AdminBoardRepository adminPostRepository;
	private final FileRepository fileRepository;

	private final JPAQueryFactory queryFactory;

	/* application.yml file 경로 **/
	@Value("${site.upload}")
	private String FILE_UPLOAD_PATH;

	// 수정시간이 바뀌게되는 이슈로 인해 querydsl로 세부 조작
	@Transactional
	public void saveCount(Long id) {
		QAdminBoardEntity qAdminPostEntity = QAdminBoardEntity.adminBoardEntity;
		queryFactory.update(qAdminPostEntity)
					.set(qAdminPostEntity.count, qAdminPostEntity.count.add(1))
					.where(qAdminPostEntity.id.eq(id))
					.execute();
	}

	@Transactional
	public void save(AdminBoardEntity post) {
		if (ObjectUtils.isNotEmpty(post.getId())) { // post.id 값이 비어있지 않은지 확인 (비어있으면 등록상황이다.)
			AdminBoardEntity	findPost = adminPostRepository.findById(post.getId()).orElseThrow(); // 수정 전에 저장된 post 객체를 찾는다.
			if (ObjectUtils.isNotEmpty(findPost.getFileEntity()) // 수정전 post 객체의 파일이 비어있는지 확인
					&& ObjectUtils.notEqual(post.getFileEntity().getFileSeqno(), // 저장할 파일과 저장 되어있는 파일의 id값 일치여부 확인
							findPost.getFileEntity().getFileSeqno())) { 
				fileRepository.deleteById(findPost.getFileEntity().getFileSeqno()); // 조건에 모두 만족하는 파일 데이터를 삭제 (수정되어 필요없는 파일)
			}
		} else {
			post.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
		}
		adminPostRepository.save(post);
	}

	public Page<AdminBoardEntity> findAll(Pageable page, AdminBoardRequest srch) {
		return adminPostRepository.findAll(AdminSearchSpecification.searchAdminBoardSpecification(srch), page);
    }

    public List<AdminBoardEntity> findAll() {
		return adminPostRepository.findAll();
    }

    public AdminBoardEntity findById(long boardSeqno) {
		return adminPostRepository.findById(boardSeqno)
				.orElseThrow(() -> new BizException("Data is Not Found", ErrorCode.NOT_FOUND));
    }

    public AdminBoardEntity findById(Long id, Authentication auth) {
    	AdminBoardEntity adminPostEntity = adminPostRepository.findById(id).orElseThrow();
		return adminPostEntity;
    }

    public void deleteById(Long id) {
    	AdminBoardEntity findPost = findById(id);
    	if(ObjectUtils.isNotEmpty(findPost.getFileEntity())) {
    		Path path = Paths.get(FILE_UPLOAD_PATH).resolve(findPost.getFileEntity().getFileNm());
//        	if (file.exists()) {
    			path.toFile().delete();
//        	}
    	}
    	adminPostRepository.deleteById(id);
    }

    public void deleteAllByIdInBatch(Iterable<Long> ids) {
    	Iterable<AdminBoardEntity> findPost = adminPostRepository.findAllById(ids);
    	if (findPost.iterator().hasNext()) {
    		if(ObjectUtils.isNotEmpty(findPost.iterator().next().getFileEntity())) {
    			Path path = Paths.get(FILE_UPLOAD_PATH).resolve(findPost.iterator().next().getFileEntity().getFileNm());
	    		path.toFile().delete();
    		}
    	}
    	adminPostRepository.deleteAllByIdInBatch(ids);
    }
}