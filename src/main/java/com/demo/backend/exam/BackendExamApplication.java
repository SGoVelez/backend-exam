package com.demo.backend.exam;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendExamApplication {

	@Bean
	public ModelMapper modelMapper() {
		// Skip null values when mapping
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true);

		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendExamApplication.class, args);
	}

}
