package io.makershive;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import io.makershive.model.AppComponent;
import io.makershive.model.Employee;
import io.makershive.repository.AppComponentRepository;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppComponentControllerTests {

	@Autowired
	private WebTestClient webTestClient;
	@Autowired
	private AppComponentRepository appComponentRepository;

	@Test
	public void addAppComponent() {
		AppComponent appComponent = appComponentRepository.save(new AppComponent()).block();
		webTestClient.post().uri("/appComponents").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(appComponent), AppComponent.class).exchange()
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
				.jsonPath("$.id").isNotEmpty();
	}

	@Test
	public void getAllAppComponent() {
		webTestClient.get().uri("/appComponents").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus()
				.isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Employee.class);
	}

	@Test
	public void getAppComponentById() {
		AppComponent appComponent = appComponentRepository.save(new AppComponent()).block();

		webTestClient.get().uri("/appComponents/{id}", Collections.singletonMap("id", appComponent.getId())).exchange()
				.expectStatus().isOk().expectBody()
				.consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	public void deleteAppComponentById() {
		AppComponent appComponent = appComponentRepository.save(new AppComponent()).block();

		webTestClient.delete().uri("/appComponents/{id}", Collections.singletonMap("id", appComponent.getId()))
				.exchange().expectStatus().isOk();
	}

	@Test
	public void updateAppComponent() {
		AppComponent appComponent = appComponentRepository.save(new AppComponent()).block();

		AppComponent newAppComponent = new AppComponent();

		webTestClient.put().uri("appComponents/{id}", Collections.singletonMap("id", appComponent.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(newAppComponent), AppComponent.class).exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8).expectBody();
	}
}
