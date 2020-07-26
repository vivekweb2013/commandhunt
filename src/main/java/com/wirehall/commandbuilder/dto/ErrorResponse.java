package com.wirehall.commandbuilder.dto;

import java.util.List;

public class ErrorResponse {

  private String message;
  private List<String> details;

  public ErrorResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<String> getDetails() {
    return details;
  }

  public void setDetails(List<String> details) {
    this.details = details;
  }

  @Override
  public String toString() {
    return "ErrorResponse{" + "message='" + message + '\'' + ", details=" + details + '}';
  }
}
