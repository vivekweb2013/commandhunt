package com.wirehall.commandbuilder.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthProcessingException extends AuthenticationException {
  public OAuth2AuthProcessingException(String msg, Throwable t) {
    super(msg, t);
  }

  public OAuth2AuthProcessingException(String msg) {
    super(msg);
  }
}
