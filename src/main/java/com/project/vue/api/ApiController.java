package com.project.vue.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vue.api.covid.ConfirmedResponse;
import com.project.vue.common.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/rest")
@RequiredArgsConstructor
public class ApiController {

	private final RestTemplate restTemplate;
	
	private static final ObjectMapper OM = new ObjectMapper();

	/** 코로나 확진자 조회 인증키*/
	@Value("${api.covid.confirmedServiceKey}")
	private String serviceKey;

	/**
	 * 공공데이터 포털 사이트 
	 * 코로나 확진자 데이터 조회 API
	 * @return JSON String
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@GetMapping("confirmed")
	public ResponseEntity<ConfirmedResponse> findConfirmedJSON() throws JsonMappingException, JsonProcessingException {
		log.trace("api/v1/rest/confirmed - get");
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme("http")
				.host("apis.data.go.kr")
				.path("/1790387/covid19CurrentStatusConfirmations/covid19CurrentStatusConfirmationsJson")
				.queryParam("serviceKey", serviceKey).build(false); // false -> Encoding 사용하지 않음

		return ResponseEntity.ok(OM.readValue(
					restTemplate.getForObject(uriComponents.toUriString(), String.class), ConfirmedResponse.class));
	}
}
