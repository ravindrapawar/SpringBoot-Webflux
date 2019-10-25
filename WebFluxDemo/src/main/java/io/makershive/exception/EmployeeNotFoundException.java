package io.makershive.exception;

@SuppressWarnings("serial")
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String employeeId) {
        super("Employee not found with id " + employeeId);
    }
}