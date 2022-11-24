package com.project.vue.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.project.vue.common.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/file")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@GetMapping("download/{id}")
	public ResponseEntity<StreamingResponseBody> download(@PathVariable Long id) {
		log.debug("api/v1/file/download - gets - id : {}", id);
		try (ByteArrayOutputStream bs = new ByteArrayOutputStream()) {
			/* 파일 다운로드 로직 **/
			String orgFileName = fileService.dwld(id, bs);
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					/* attachement -> 로컬에 저장, filename -> 파일 이름 **/
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + new String(orgFileName.getBytes("UTF-8"), "ISO-8859-1") +"\"")
					.body(os -> os.write(bs.toByteArray()));
		} catch(IOException e) { // TODO Exception
			return new ResponseEntity<StreamingResponseBody>(HttpStatus.CONFLICT);
		}
	}
}
