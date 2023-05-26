package com.project.vue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    Docket api() {
		return new Docket(DocumentationType.OAS_30) // 3.0 버전
				.apiInfo(apiInfo()) // Swagger API 문서에 대한 설명을 표기
				.select() // 빌더 초기화
				.apis(RequestHandlerSelectors.any()) // Controller 를 포함하고 있는 패키지의 경로를 지정
				.paths(PathSelectors.any()) // 특정 Path 를 지정하여 apis() 에 지정된 경로 중에서 원하는 경로의 api만 문서화
				.build();
	}

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot learn-vue Rest API Documentation")
                .description("API 문서")
                .contact(new Contact("KoeyNim", "https://github.com/KoeyNim", "konimel19@gmail.com")) // 작성자 정보
                .version("0.1")
//                .license("license")
//                .licenseUrl("licenseUrl")
//                .termsOfServiceUrl("termsOfServiceUrl")
//                .extensions(null)
                .build();
    }
}
