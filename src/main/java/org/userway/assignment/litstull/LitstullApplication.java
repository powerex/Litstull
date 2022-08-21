package org.userway.assignment.litstull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableCaching
@EnableMongoAuditing
@SpringBootApplication
public class LitstullApplication {

	public static void main(String[] args) {
		SpringApplication.run(LitstullApplication.class, args);
	}

}
