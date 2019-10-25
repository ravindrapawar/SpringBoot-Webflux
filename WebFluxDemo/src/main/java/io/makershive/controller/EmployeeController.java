package io.makershive.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.makershive.exception.EmployeeNotFoundException;
import io.makershive.exception.ErrorResponse;
import io.makershive.model.Employee;
import io.makershive.repository.EmployeeRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;

	@GetMapping
	public Flux<Employee> getAllEmployee() {
		return employeeRepository.findAll();
	}

	@PostMapping
	public Mono<Employee> createEmployee(@RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Employee>> getEmployeeById(@PathVariable(value = "id") String id) throws EmployeeNotFoundException {
		return employeeRepository.findById(id).map(savedEmployee -> ResponseEntity.ok(savedEmployee))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/email/{email}")
	public Flux<ResponseEntity<Employee>> getEmployeeByEmail(@PathVariable(value = "email") String email) throws EmployeeNotFoundException {
		return employeeRepository.findByEmail(email).map(savedEmployee -> ResponseEntity.ok(savedEmployee))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteEmployee(@PathVariable(value = "id") String employeeId) {

		return employeeRepository.findById(employeeId)
				.flatMap(existingEmployee -> employeeRepository.delete(existingEmployee))
				.then(Mono.just((new ResponseEntity<Void>(HttpStatus.OK)))
						.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)));

	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Employee>> updateEmployee(@PathVariable(value = "id") String employeeId,
			@RequestBody Employee employee) {
		return employeeRepository.findById(employeeId).flatMap(existingEmployee -> {
			existingEmployee.setName(employee.getName());
			return employeeRepository.save(existingEmployee);
		}).map(updatedTweet -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// All Employees are Sent to the client as Server Sent Events
	@GetMapping(value = "/stream/employees", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Employee> streamAllEmployees() {
		return Flux.zip(Flux.interval(Duration.ofSeconds(2)), employeeRepository.findAll()).map(Tuple2::getT2);
	}

	/*
	 * Exception Handling Examples (These can be put into a @ControllerAdvice to
	 * handle exceptions globally)
	 */

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity handleDuplicateKeyException(DuplicateKeyException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse("A Employee with the same text already exists"));
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
		return ResponseEntity.notFound().build();
	}
}
