package io.makershive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableMongoAuditing
public class WebFluxDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxDemoApplication.class, args);
	}

}
