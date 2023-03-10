package com.project.vue.api.payload;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfirmedResponse {

    private Response response;

    @Getter
    @NoArgsConstructor
    public static class Response {
      /** 결과 리스트 */
      private List<Data> result;
  	  /** 결과 개수 */
      private int resultCnt;
  	  /** 결과코드 */
      private int resultCode;
  	  /** 결과메시지 */
      private String resultMsg;
    }

    @Getter
    @NoArgsConstructor
    public static class Data {
    	/** 데이터 조회 기준 일시 */
    	private String mmddhh;

    	/** 데이터 조회 기준일 -6일 */
    	private String mmdd1;
    	/** 일일 */
    	private int cnt1;
    	/** 인구 10만명당 */
    	private BigDecimal rate1;

    	/** 데이터 조회 기준일 -5일 */
    	private String mmdd2;
    	/** 일일 */
    	private int cnt2;
    	/** 인구 10만명당 */
    	private BigDecimal rate2;

    	/** 데이터 조회 기준일 -4일 */
    	private String mmdd3;
    	/** 일일 */
    	private int cnt3;
    	/** 인구 10만명당 */
    	private BigDecimal rate3;

    	/** 데이터 조회 기준일 -3일 */
    	private String mmdd4;
    	/** 일일 */
    	private int cnt4;
    	/** 인구 10만명당 */
    	private BigDecimal rate4;

    	/** 데이터 조회 기준일 -2일 */
    	private String mmdd5;
    	/** 일일 */
    	private int cnt5;
    	/** 인구 10만명당 */
    	private BigDecimal rate5;

    	/** 데이터 조회 기준일 -1일 */
    	private String mmdd6;
    	/** 일일 */
    	private int cnt6;
    	/** 인구 10만명당 */
    	private BigDecimal rate6;

    	/** 데이터 조회 기준일 */
    	private String mmdd7;
    	/** 일일 */
    	private int cnt7;
    	/** 인구 10만명당 */
    	private BigDecimal rate7;

    	/** 주간일평균 */
    	private String mmdd8;
    	/** 일일 */
    	private int cnt8;
    	/** 인구 10만명당 */
    	private BigDecimal rate8;
    }
}
