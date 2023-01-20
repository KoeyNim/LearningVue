package com.project.vue.admin.post;

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

import com.project.vue.file.FileRepository;
import com.project.vue.specification.SearchSpecification;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminPostService {
	
	private final AdminPostRepository adminPostRepository;
	
	private final FileRepository fileRepository;
	
	private final JPAQueryFactory queryFactory;
	
	/* application.yml file 경로 **/
	@Value("${site.upload}")
	private String FILE_UPLOAD_PATH;
	
	// 수정시간이 바뀌게되는 이슈로 인해 querydsl로 세부 조작
	@Transactional
	public void saveCount(Long id) {
		QAdminPostEntity qAdminPostEntity = QAdminPostEntity.adminPostEntity;
		queryFactory.update(qAdminPostEntity)
					.set(qAdminPostEntity.count, qAdminPostEntity.count.add(1))
					.where(qAdminPostEntity.id.eq(id))
					.execute();
	}
	
	@Transactional
	public void save(AdminPostEntity post) {
		if (ObjectUtils.isNotEmpty(post.getId())) { // post.id 값이 비어있지 않은지 확인 (비어있으면 등록상황이다.)
			AdminPostEntity	findPost = adminPostRepository.findById(post.getId()).orElseThrow(); // 수정 전에 저장된 post 객체를 찾는다.
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
	
	public Page<AdminPostEntity> findAll(Pageable page, AdminPostRequest srch) {
		return adminPostRepository.findAll(SearchSpecification.searchAdminPostSpecification(srch), page);
    }
    
    public List<AdminPostEntity> findAll() {
		return adminPostRepository.findAll();
    }
    
    public AdminPostEntity findById(Long id) {
		return adminPostRepository.findById(id).orElseThrow();
    }
    
    public AdminPostEntity findById(Long id, Authentication auth) {
    	AdminPostEntity adminPostEntity = adminPostRepository.findById(id).orElseThrow();
		return adminPostEntity;
    }
    
    public void deleteById(Long id) {
    	AdminPostEntity findPost = findById(id);
    	if(ObjectUtils.isNotEmpty(findPost.getFileEntity())) {
    		Path path = Paths.get(FILE_UPLOAD_PATH).resolve(findPost.getFileEntity().getFileNm());
//        	if (file.exists()) {
    			path.toFile().delete();
//        	}
    	}
    	adminPostRepository.deleteById(id);
    }
    
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
    	Iterable<AdminPostEntity> findPost = adminPostRepository.findAllById(ids);
    	if (findPost.iterator().hasNext()) {
    		if(ObjectUtils.isNotEmpty(findPost.iterator().next().getFileEntity())) {
    			Path path = Paths.get(FILE_UPLOAD_PATH).resolve(findPost.iterator().next().getFileEntity().getFileNm());
	    		path.toFile().delete();
    		}
    	}
    	adminPostRepository.deleteAllByIdInBatch(ids);
    }
}