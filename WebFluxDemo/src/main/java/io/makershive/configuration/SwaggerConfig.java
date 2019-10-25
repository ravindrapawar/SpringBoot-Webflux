package io.makershive.configuration;
import java.util.Collections;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <h1>SwaggerConfig</h1>
 * <p>
 * This is the SwaggerConfig, it consists of docket bean and api-info
 * </p>
 * 
 * @author Shanawaz
 * @author Ravindra Pawar
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements HealthIndicator {

	ApiInfo apiInfo;

	/**
	 * <p>
	 * This method returns the docket bean object by setting up the values of apis
	 * and path
	 * </p>
	 * 
	 * @return the Docket
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("io.makershive.controller"))
				.paths(PathSelectors.regex("/*.*")).build().apiInfo(metaData());
	}

	/**
	 * <p>
	 * This method returns the ApiInfo
	 * </p>
	 * 
	 * @return the ApiInfo
	 */

	private ApiInfo metaData() {
		apiInfo = new ApiInfo("EverGreen Revolution", "EverGreen Revolution Services", "1.0", "Terms of Services",
				new Contact("MakersHive", "makershive.io", "makershive@gmail.com"), "Apache License", "www.google.com",
				Collections.emptyList());
		return apiInfo;
	}

	@Override
	public Health health() {
		// TODO Auto-generated method stub
		int errorCode = check(); // perform some specific health check
		if (errorCode != 0) {
			return Health.down().withDetail("Error Code", errorCode).build();
		}
		return Health.up().build();
	}

	public int check() {
		// Our logic to check health
		return 0;
	}
}
