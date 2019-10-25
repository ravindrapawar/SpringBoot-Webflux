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

import io.makershive.model.Employee;
import io.makershive.repository.EmployeeRepository;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebFluxDemoApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	EmployeeRepository employeeRepository;

	@Test
	public void getAllEmployee() {
		webTestClient.get().uri("/employees").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Employee.class);
	}

	@Test
	public void createEmployee() {
		Employee employee = new Employee();

		webTestClient.post().uri("/employees").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(employee), Employee.class).exchange()
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
				.jsonPath("$.id").isNotEmpty();
	}

	@Test
	public void getEmployeeById() {
		Employee employee = employeeRepository.save(new Employee()).block();

		webTestClient.get().uri("/employees/{id}", Collections.singletonMap("id", employee.getId())).exchange()
				.expectStatus().isOk().expectBody()
				.consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	public void getEmployeeByEmail() {
		Employee employee = employeeRepository.save(new Employee()).block();

		webTestClient.get().uri("/employees/{email}", Collections.singletonMap("email", employee.getEmail())).exchange()
				.expectStatus().isOk().expectBody()
				.consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	public void updateEmployee() {
		Employee employee = employeeRepository.save(new Employee()).block();

		Employee newEmployeeData = new Employee();

		webTestClient.put().uri("employees/{id}", Collections.singletonMap("id", employee.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(newEmployeeData), Employee.class).exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8).expectBody();
	}

	@Test
	public void deleteEmployee() {
		Employee employee = employeeRepository.save(new Employee()).block();

		webTestClient.delete().uri("/employees/{id}", Collections.singletonMap("id", employee.getId())).exchange()
				.expectStatus().isOk();
	}
}
