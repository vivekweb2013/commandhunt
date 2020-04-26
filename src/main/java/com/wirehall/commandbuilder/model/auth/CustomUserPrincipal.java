package com.wirehall.commandbuilder.model.auth;

import com.wirehall.commandbuilder.dto.User;
import com.wirehall.commandbuilder.model.props.USER_PROPERTY;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomUserPrincipal implements OAuth2User, UserDetails {
  private final Collection<? extends GrantedAuthority> authorities;
  private final User user;
  private final String password;
  private Map<String, Object> attributes;

  public CustomUserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
    this.password = (String) user.getProperties().remove(USER_PROPERTY.password);
    this.user = user;
    this.authorities = authorities;
  }

  public static CustomUserPrincipal create(User user) {
    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    return new CustomUserPrincipal(user, authorities);
  }

  public static CustomUserPrincipal create(User user, Map<String, Object> attributes) {
    CustomUserPrincipal customUserPrincipal = CustomUserPrincipal.create(user);
    customUserPrincipal.setAttributes(attributes);
    return customUserPrincipal;
  }

  public User getUser() {
    return this.user;
  }

  public Object getId() {
    return user.getId();
  }

  public String getEmail() {
    return (String) user.getProperty(USER_PROPERTY.email);
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return (String) user.getProperty(USER_PROPERTY.email);
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
    return String.valueOf(this.user.getId());
  }
}
