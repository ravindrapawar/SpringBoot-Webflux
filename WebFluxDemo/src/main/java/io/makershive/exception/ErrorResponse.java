package io.makershive.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	private String message;
}
