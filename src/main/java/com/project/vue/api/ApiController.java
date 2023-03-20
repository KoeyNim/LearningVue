package com.project.vue.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vue.api.payload.ConfirmedResponse;
import com.project.vue.api.payload.VilageFcstInfoRequest;
import com.project.vue.api.payload.VilageFcstInfoResponse;
import com.project.vue.common.Constants;
import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.CustomExceptionHandler.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/rest")
@RequiredArgsConstructor
public class ApiController {

	private final RestTemplate restTemplate;

	private static final ObjectMapper OM = new ObjectMapper();

	private static final String PUBLIC_DATA_SCHEME = "http";
	private static final String PUBLIC_DATA_HOST = "apis.data.go.kr";

	/** 공공데이터 API 인증키 */
	@Value("${api.publicData.ServiceKey}")
	private String serviceKey;

	/**
	 * 공공데이터 포털 사이트 
	 * 코로나 확진자 데이터 조회 API
	 * @return ResponseEntity<ConfirmedResponse>
	 * @throws JsonProcessingException
	 */
	@GetMapping("confirmed")
	public ResponseEntity<ConfirmedResponse> findConfirmedJSON() {
		log.trace("api/v1/rest/confirmed - gets");
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme(PUBLIC_DATA_SCHEME)
				.host(PUBLIC_DATA_HOST)
				.path("/1790387/covid19CurrentStatusConfirmations/covid19CurrentStatusConfirmationsJson")
				.queryParam("serviceKey", serviceKey).build(false); // false -> Encoding 사용하지 않음
		try {
			return ResponseEntity.ok(OM.readValue(
					restTemplate.getForObject(uriComponents.toUriString(), String.class), ConfirmedResponse.class));
		} catch (JsonProcessingException ex) {
			throw new BizException("JSON Deserialize Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 공공데이터 포털 사이트 
	 * 기상청 단기예보 데이터 조회 API
	 * @return JSON String
	 * @throws JsonProcessingException
	 */
	@GetMapping("vilage-fcst-info")
	public ResponseEntity<VilageFcstInfoResponse> findVilageFcstInfoJSON(@RequestBody VilageFcstInfoRequest req) {
		log.trace("api/v1/rest/vilage-fcst-info - gets - req : {}", req);
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme(PUBLIC_DATA_SCHEME)
				.host(PUBLIC_DATA_HOST)
				.path("/1360000/VilageFcstInfoService_2.0/getVilageFcst")
				.queryParam("serviceKey", serviceKey)// false -> Encoding 사용하지 않음
				.queryParam("pageNo", req.getPageNo())
				.queryParam("numOfRows", req.getNumOfRows())
				.queryParam("dataType", "JSON")
				.queryParam("base_date", req.getBaseDate())
				.queryParam("base_time", req.getBaseTime())
				.queryParam("nx", req.getNx())
				.queryParam("ny", req.getNy()).build(false);
		try {
			return ResponseEntity.ok(OM.readValue(
					restTemplate.getForObject(uriComponents.toUriString(), String.class), VilageFcstInfoResponse.class));
		} catch (JsonProcessingException ex) {
			throw new BizException("JSON Deserialize Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
