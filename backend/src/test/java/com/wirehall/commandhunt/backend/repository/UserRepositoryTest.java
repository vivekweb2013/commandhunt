package com.wirehall.commandhunt.backend.repository;

import com.wirehall.commandhunt.backend.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void should_InsertEntity_When_NotMandatoryFieldsAreNull() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail("abc@xyz.com");
        userEntity.setEmailVerified(false);
        userEntity.setJoinedOn(new Timestamp(System.currentTimeMillis()));
        userEntity.setProvider(UserEntity.OAuthProvider.LOCAL);

        userEntity.setPassword(null); // Password will be null in case of Social Login
        userEntity.setProviderId(null); // Provider Id will be null in case of local login
        userEntity.setImageUrl(null); // Image url will be null on local login

        userRepository.saveAndFlush(userEntity);
        List<UserEntity> userEntities = userRepository.findAll();

        assertEquals(1, userEntities.size());
        UserEntity userEntitySaved = userEntities.get(0);
        assertEquals(userEntity.getName(), userEntitySaved.getName());
        assertEquals(userEntity.getEmail(), userEntitySaved.getEmail());
        assertEquals(userEntity.isEmailVerified(), userEntitySaved.isEmailVerified());
        assertEquals(userEntity.getPassword(), userEntitySaved.getPassword());
        assertEquals(userEntity.getImageUrl(), userEntitySaved.getImageUrl());
        assertEquals(userEntity.getJoinedOn(), userEntitySaved.getJoinedOn());
        assertEquals(userEntity.getProvider(), userEntitySaved.getProvider());
        assertEquals(userEntity.getProviderId(), userEntitySaved.getProviderId());
    }

    @Test
    void should_MatchWithRetrievedEntity_When_SameWasInserted() {
        UserEntity userEntity = generateUserEntity();

        userRepository.saveAndFlush(userEntity);
        List<UserEntity> userEntities = userRepository.findAll();

        assertEquals(1, userEntities.size());
        UserEntity userEntitySaved = userEntities.get(0);
        assertEquals(userEntity.getName(), userEntitySaved.getName());
        assertEquals(userEntity.getEmail(), userEntitySaved.getEmail());
        assertEquals(userEntity.isEmailVerified(), userEntitySaved.isEmailVerified());
        assertEquals(userEntity.getPassword(), userEntitySaved.getPassword());
        assertEquals(userEntity.getImageUrl(), userEntitySaved.getImageUrl());
        assertEquals(userEntity.getJoinedOn(), userEntitySaved.getJoinedOn());
        assertEquals(userEntity.getProvider(), userEntitySaved.getProvider());
        assertEquals(userEntity.getProviderId(), userEntitySaved.getProviderId());
    }

    @Test
    void should_ThrowException_When_InsertedWithoutName() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(null);
        userEntity.setEmail("abc@xyz.com");
        userEntity.setEmailVerified(false);
        userEntity.setJoinedOn(new Timestamp(System.currentTimeMillis()));
        userEntity.setProvider(UserEntity.OAuthProvider.LOCAL);

        Executable e = () -> userRepository.saveAndFlush(userEntity);

        assertThrows(DataIntegrityViolationException.class, e);
    }

    @Test
    void should_ThrowException_When_InsertedWithoutEmail() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail(null);
        userEntity.setEmailVerified(false);
        userEntity.setJoinedOn(new Timestamp(System.currentTimeMillis()));
        userEntity.setProvider(UserEntity.OAuthProvider.LOCAL);

        Executable e = () -> userRepository.saveAndFlush(userEntity);

        assertThrows(Exception.class, e);
    }

    @Test
    void should_ThrowException_When_InsertedWithoutJoinedOn() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail("abc@xyz.com");
        userEntity.setEmailVerified(false);
        userEntity.setJoinedOn(null);
        userEntity.setProvider(UserEntity.OAuthProvider.LOCAL);

        Executable e = () -> userRepository.saveAndFlush(userEntity);

        assertThrows(DataIntegrityViolationException.class, e);
    }

    @Test
    void should_ThrowException_When_InsertedWithoutProvider() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail("abc@xyz.com");
        userEntity.setEmailVerified(false);
        userEntity.setJoinedOn(new Timestamp(System.currentTimeMillis()));
        userEntity.setProvider(null);

        Executable e = () -> userRepository.saveAndFlush(userEntity);

        assertThrows(DataIntegrityViolationException.class, e);
    }

    private UserEntity generateUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail("abc@xyz.com");
        userEntity.setEmailVerified(true);
        userEntity.setPassword("secret");
        userEntity.setImageUrl("/user.jpg");
        userEntity.setJoinedOn(new Timestamp(System.currentTimeMillis()));
        userEntity.setProvider(UserEntity.OAuthProvider.GOOGLE);
        userEntity.setProviderId("12345");

        return userEntity;
    }
}