package io.makershive.model;

import org.springframework.data.mongodb.core.mapping.Document;

import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@SuperBuilder(toBuilder=true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "employees")
public class Employee {

	@Id
	private String id;
	private String name;
	private String address;
	private long phoneNumber;
	private String dob;
	private String email;
	private Student student;
	
}
