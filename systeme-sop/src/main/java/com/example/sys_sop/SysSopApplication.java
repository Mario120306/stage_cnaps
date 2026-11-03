package com.example.sys_sop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class SysSopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SysSopApplication.class, args);
	}

	@Bean(name = "pdfTaskExecutor")
	public ThreadPoolTaskExecutor pdfTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("pdf-generation-");
		executor.initialize();
		return executor;
	}

}
