package com.wirehall.commandhunt.backend.model.auth;

import com.wirehall.commandhunt.backend.model.UserEntity;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomUserPrincipal implements OAuth2User, UserDetails {

  private final Collection<? extends GrantedAuthority> authorities;
  private final transient UserEntity userEntity;
  private final transient String password;
  private transient Map<String, Object> attributes;

  /**
   * Custom User Principal.
   *
   * @param userEntity  User DTO.
   * @param authorities Collection of authorities.
   */
  public CustomUserPrincipal(UserEntity userEntity,
      Collection<? extends GrantedAuthority> authorities) {
    this.password = userEntity.getPassword();
    this.userEntity = userEntity;
    this.authorities = authorities;
  }

  /**
   * Generates the principal from specified user instance.
   *
   * @param userEntity User entity.
   * @return Custom User Principal instance.
   */
  public static CustomUserPrincipal create(UserEntity userEntity) {
    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    return new CustomUserPrincipal(userEntity, authorities);
  }

  /**
   * Generates the principal from specified user instance and attributes.
   *
   * @param userEntity The user entity.
   * @param attributes Map containing attributes to be set for principal.
   * @return Custom User Principal instance.
   */
  public static CustomUserPrincipal create(UserEntity userEntity, Map<String, Object> attributes) {
    CustomUserPrincipal customUserPrincipal = CustomUserPrincipal.create(userEntity);
    customUserPrincipal.setAttributes(attributes);
    return customUserPrincipal;
  }

  public UserEntity getUser() {
    return this.userEntity;
  }

  public String getId() {
    return userEntity.getEmail();
  }

  public String getEmail() {
    return userEntity.getEmail();
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return userEntity.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String getName() {
    return String.valueOf(this.userEntity.getEmail());
  }
}
