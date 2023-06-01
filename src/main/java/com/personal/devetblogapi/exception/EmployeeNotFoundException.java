package com.personal.devetblogapi.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends RuntimeException {

  public EmployeeNotFoundException(UUID id) {
    super("EmployeeEntity with id " + id + " not found");
  }
}
