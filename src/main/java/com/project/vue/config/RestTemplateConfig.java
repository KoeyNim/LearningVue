package com.project.vue.config;

import java.nio.charset.Charset;
import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
		DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
		uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); // Auto Encoding Mode Off
        return builder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .uriTemplateHandler(uriBuilderFactory) // 설정한 옵션 부여
                .setConnectTimeout(Duration.ofMillis(20000)) // 접속 지연시간
                .setReadTimeout(Duration.ofMillis(20000)) // 읽기 지연시간
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8"))) // 메세지 컨버터
                .build();
    }
}
