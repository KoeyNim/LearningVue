package com.project.vue.file.image;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.project.vue.common.Constants;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/image")
@RequiredArgsConstructor
public class imageController {
	
	private final ImageService imageService;
	
	private final ResourceLoader resourceLoader;
	
	/* 
	 * 이미지 저장 로직
	 * 1. 이미지를 tmp 폴더에 저장
	 * 2. tmp 폴더에 있는 이미지를 에디터에 노출
	 * 3. 저장 버튼클릭시 DB에 정보 저장 후 tmp 폴더에 있는 이미지 삭제
	 * 4. 게시글 상세, 수정할 경우 DB에 있는 이미지를 가져옴
	 * **/
	
	@PostMapping("upload") // 1. return 값을 entity로 내려주고 저장시 db에 저장되게 (이미지 미리보기 안될듯..)
	public ResponseEntity<String> upload(MultipartFile image) throws Exception {
		ImageEntity imageEntity = imageService.upld(image);
		return ResponseEntity.ok(Constants.REQUEST_MAPPING_PREFIX + "/image/find/" + imageEntity.getId());
	}
	
	@GetMapping("find/{id}") // TODO temp 폴더를 생성해 미리보기로 보여주며 저장버튼을 누를시 파일 삭제 되게 구현해볼것)
	public ResponseEntity<Resource> find(@PathVariable Long id) throws Exception {
		ImageEntity imageEntity = imageService.findById(id);
		Resource resource = resourceLoader.getResource("file:" + imageEntity.getFilePath() + imageEntity.getFileNm()); // 이거 변경해야됨
		return ResponseEntity.ok(resource);
	}
}
