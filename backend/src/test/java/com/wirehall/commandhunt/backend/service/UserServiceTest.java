package com.wirehall.commandhunt.backend.service;

import com.wirehall.commandhunt.backend.dto.User;
import com.wirehall.commandhunt.backend.dto.auth.SignUp;
import com.wirehall.commandhunt.backend.exception.BadRequestException;
import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock UserRepository userRepository;

  UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, new BCryptPasswordEncoder());
  }

  @Test
  void should_AddUser_When_SignUpRequested() {
    // Given
    SignUp signUp = new SignUp();
    signUp.setName("John Doe");
    signUp.setEmail("abc@xyz.com");
    signUp.setPassword("secret");
    when(userRepository.save(ArgumentMatchers.isA(UserEntity.class))).thenReturn(new UserEntity());

    // When
    User user = userService.registerUser(signUp);

    // Then
    ArgumentCaptor<UserEntity> arg = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository).save(arg.capture());
    UserEntity ue = arg.getValue();
    Assertions.assertEquals(signUp.getName(), ue.getName());
    Assertions.assertEquals(signUp.getEmail(), ue.getEmail());
    Assertions.assertNotNull(ue.getPassword());
    Assertions.assertNotEquals(signUp.getPassword(), ue.getPassword());
    Assertions.assertNotNull(ue.getJoinedOn());
    Assertions.assertEquals(UserEntity.OAuthProvider.LOCAL, ue.getProvider());

    Assertions.assertEquals(signUp.getName(), user.getName());
    Assertions.assertEquals(signUp.getEmail(), user.getEmail());
    Assertions.assertEquals(UserEntity.OAuthProvider.LOCAL.toString(), user.getProvider());
  }

  @Test
  void should_ThrowException_When_UserWithEmailExist() {
    // Given
    SignUp signUp = new SignUp();
    signUp.setName("John Doe");
    signUp.setEmail("abc@xyz.com");
    signUp.setPassword("secret");
    when(userRepository.findById(anyString())).thenReturn(Optional.of(new UserEntity()));

    // When
    Executable e = () -> userService.registerUser(signUp);

    // Then
    assertThrows(BadRequestException.class, e);
  }
}
