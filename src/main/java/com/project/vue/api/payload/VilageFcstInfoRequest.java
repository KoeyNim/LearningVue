package com.project.vue.api.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VilageFcstInfoRequest {

	/** 페이지 번호 */
	private Long pageNo;
	/** 한 페이지 결과 수 */
	private Long numOfRows;
	/** 발표일자 ex) 20210628 */
	private Long baseDate;
	/** 발표시각 정시단위 ex) 0600 */
	private Long baseTime;
	/** 예보지점 X 좌표 */
	private Long nx;
	/** 예보지점 Y 좌표 */
	private Long ny;
}
