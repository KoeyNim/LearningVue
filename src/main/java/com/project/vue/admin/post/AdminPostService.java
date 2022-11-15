package com.project.vue.admin.post;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
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
					&& ObjectUtils.notEqual(post.getFileEntity().getId(), // 저장할 파일과 저장 되어있는 파일의 id값 일치여부 확인
							findPost.getFileEntity().getId())) { 
				fileRepository.deleteById(findPost.getFileEntity().getId()); // 조건에 모두 만족하는 파일 데이터를 삭제 (수정되어 필요없는 파일)
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
    	adminPostEntity.setAuthUserId(auth.getPrincipal());
		return adminPostEntity;
    }
    
    public void deleteById(Long id) {
    	AdminPostEntity findPost = findById(id);
    	if(ObjectUtils.isNotEmpty(findPost.getFileEntity())) {
        	File file = new File(findPost.getFileEntity().getFilePath() + findPost.getFileEntity().getFileNm());
//        	if (file.exists()) {
        		file.delete();
//        	}
    	}
    	adminPostRepository.deleteById(id);
    }
    
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
    	Iterable<AdminPostEntity> findPost = adminPostRepository.findAllById(ids);
    	if (findPost.iterator().hasNext()) {
    		if(ObjectUtils.isNotEmpty(findPost.iterator().next().getFileEntity())) {
	    		File file = new File(findPost.iterator().next().getFileEntity().getFilePath() + 
						 findPost.iterator().next().getFileEntity().getFileNm());
	    		file.delete();
    		}
    	}
    	adminPostRepository.deleteAllByIdInBatch(ids);
    }
}