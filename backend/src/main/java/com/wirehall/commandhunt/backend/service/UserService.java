package com.wirehall.commandhunt.backend.service;

import com.wirehall.commandhunt.backend.dto.User;
import com.wirehall.commandhunt.backend.dto.auth.SignUp;
import com.wirehall.commandhunt.backend.exception.BadRequestException;
import com.wirehall.commandhunt.backend.mapper.UserMapper;
import com.wirehall.commandhunt.backend.model.UserEntity;
import com.wirehall.commandhunt.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
@Transactional
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper = new UserMapper();

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Adds the user created as a result of sign up request.
     *
     * @param signUpRequest Sign up request payload.
     * @return User DTO.
     */
    public User registerUser(SignUp signUpRequest) {
        if (!StringUtils.hasLength(signUpRequest.getPassword())) {
            LOGGER.error("Empty password in sign up request: {}", signUpRequest.getEmail());
            throw new BadRequestException("Password is empty in the sign up request");
        }
        if (userRepository.findById(signUpRequest.getEmail()).isPresent()) {
            LOGGER.error("Email: {} is already in use", signUpRequest.getEmail());
            throw new BadRequestException("Email address already in use.");
        }

        UserEntity userEntity = mapper.mapToUserEntity(signUpRequest, passwordEncoder);
        userEntity.setJoinedOn(new Timestamp(System.currentTimeMillis()));
        userRepository.save(userEntity);
        return mapper.mapToUser(userEntity);
    }
}
