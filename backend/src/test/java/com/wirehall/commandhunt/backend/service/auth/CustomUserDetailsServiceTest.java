package com.wirehall.commandhunt.backend.service.auth;

import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
  @Mock UserRepository userRepository;

  CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setUp() {
    customUserDetailsService = new CustomUserDetailsService(userRepository);
  }

  @Test
  void should_LoadUser_When_ValidEmailProvided() {
    when(userRepository.findById(anyString())).thenReturn(Optional.of(new UserEntity()));
    Executable e = () -> customUserDetailsService.loadUserByUsername(anyString());
    assertDoesNotThrow(e);
  }

  @Test
  void should_ThrowException_When_NoUserWithEmailFound() {
    when(userRepository.findById(anyString())).thenReturn(Optional.empty());
    Executable e = () -> customUserDetailsService.loadUserByUsername(anyString());
    assertThrows(BadCredentialsException.class, e);
  }
}
