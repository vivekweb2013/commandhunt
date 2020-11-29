package com.wirehall.commandbuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  private final String resourceName;
  private final String fieldName;
  private final Object fieldValue;

  /**
   * Exception class used in case of requested resource is not found.
   *
   * @param resourceName The name of the resource.
   * @param fieldName    The name of the field.
   * @param fieldValue   The value of the field.
   */
  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public String getResourceName() {
    return resourceName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public Object getFieldValue() {
    return fieldValue;
  }
}
