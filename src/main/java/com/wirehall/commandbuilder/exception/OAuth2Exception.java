package com.wirehall.commandbuilder.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2Exception extends AuthenticationException {
  public OAuth2Exception(String msg, Throwable t) {
    super(msg, t);
  }

  public OAuth2Exception(String msg) {
    super(msg);
  }
}
