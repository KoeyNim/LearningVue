package com.project.vue.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.project.vue.file.FileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {
	
	private final FileRepository fileRepository;
	
//	@Scheduled(cron = "*/20 * * * * *")
//	public void printJob() {
//		log.info(new Date().toString());
//	}
	
//	@Scheduled(cron = "10 * * * * *")
//	public void cleanGarbageFile() {
//		log.info(new Date().toString());
//		
//	}
}
