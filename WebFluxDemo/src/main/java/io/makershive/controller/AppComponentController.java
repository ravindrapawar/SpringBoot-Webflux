package io.makershive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.makershive.model.AppComponent;
import io.makershive.repository.AppComponentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/appComponents")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_FARMER')")
public class AppComponentController {

	@Autowired
	AppComponentRepository appComponentRepository;

	@GetMapping
	public Flux<AppComponent> getAllAppComponent() {
		return appComponentRepository.findAll();
	}

	@PostMapping
	public Mono<AppComponent> addAppComponent(@RequestBody AppComponent appComponent) {
		return appComponentRepository.save(appComponent);
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<AppComponent>> getAppComponentById(@PathVariable(value = "id") String id)
			{
		return appComponentRepository.findById(id).map(savedAppComponent -> ResponseEntity.ok(savedAppComponent))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ResponseEntity<Void>> deleteAppComponentById(@PathVariable(value = "id") String id) {
		return appComponentRepository.findById(id)
				.flatMap(existingAppComponent -> appComponentRepository.delete(existingAppComponent))
				.then(Mono.just((new ResponseEntity<Void>(HttpStatus.OK)))
						.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)));

	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ResponseEntity<AppComponent>> updateAppComponent(@PathVariable(value = "id") String id,
			@RequestBody AppComponent appComponent) {
		return appComponentRepository.findById(id).flatMap(existingAppComponent -> {
			existingAppComponent.setName(appComponent.getName());
			return appComponentRepository.save(existingAppComponent);
		}).map(updatedAppComponent -> new ResponseEntity<>(updatedAppComponent, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}
}
