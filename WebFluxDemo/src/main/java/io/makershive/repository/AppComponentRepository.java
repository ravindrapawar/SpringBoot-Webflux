package io.makershive.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import io.makershive.model.AppComponent;
import reactor.core.publisher.Flux;

@Repository
public interface AppComponentRepository extends ReactiveMongoRepository<AppComponent, String> {

		@Query("{ 'name': ?0 }")
	    Flux<AppComponent> findByName(final String name);
}
