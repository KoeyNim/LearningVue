package com.project.vue.common.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.project.vue.common.Constants;
import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/file")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	/**
	 * 첨부파일 다운로드
	 * @param id file 기본키
	 * @return ResponseEntity<StreamingResponseBody>
	 */
	@GetMapping("download/{id}")
	public ResponseEntity<StreamingResponseBody> dwld(@PathVariable Long id) {
		log.debug("api/v1/file/download - gets - id : {}", id);
		try (ByteArrayOutputStream bs = new ByteArrayOutputStream()) {
			String orgFileName = fileService.dwld(id, bs);
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					/** attachement -> 로컬에 저장, filename -> 파일 이름 */
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + new String(orgFileName.getBytes("UTF-8"), "ISO-8859-1") +"\"")
					.body(os -> os.write(bs.toByteArray()));
		} catch(IOException ex) {
			throw new BizException("File Download Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
