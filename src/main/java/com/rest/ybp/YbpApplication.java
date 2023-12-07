package com.rest.ybp;

import com.rest.ybp.extractor.Extractor;
import com.rest.ybp.extractor.SingleVideoExtractor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YbpApplication {

	public static void main(String[] args) {
		SpringApplication.run(YbpApplication.class, args);
	}
}
