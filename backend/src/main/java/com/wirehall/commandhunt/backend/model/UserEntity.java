package com.wirehall.commandhunt.backend.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appuser") // 'User' is a reserved keyword in postgres
public class UserEntity {

  @Id
  private String email;
  @Column(nullable = false)
  private String name;

  private boolean emailVerified;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OAuthProvider provider;
  private String providerId;
  private String imageUrl;
  private String password;
  @Column(nullable = false)
  private Timestamp joinedOn;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public OAuthProvider getProvider() {
    return provider;
  }

  public void setProvider(OAuthProvider provider) {
    this.provider = provider;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Timestamp getJoinedOn() {
    return joinedOn;
  }

  public void setJoinedOn(Timestamp joinedOn) {
    this.joinedOn = joinedOn;
  }

  @Override
  public String toString() {
    return "UserEntity{" +
        "email='" + email + '\'' +
        ", name='" + name + '\'' +
        ", emailVerified=" + emailVerified +
        ", provider=" + provider +
        ", providerId='" + providerId + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", password='" + password + '\'' +
        ", joinedOn=" + joinedOn +
        '}';
  }

  public enum OAuthProvider {LOCAL, FACEBOOK, GOOGLE, GITHUB}
}

