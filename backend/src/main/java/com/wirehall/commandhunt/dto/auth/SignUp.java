package com.wirehall.commandhunt.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SignUp {

  @NotBlank
  private String name;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "SignUp{" + "name='" + name + '\'' + ", email='" + email + '\'' + '}';
  }
}
