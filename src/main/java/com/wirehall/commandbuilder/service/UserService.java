package com.wirehall.commandbuilder.service;

import com.wirehall.commandbuilder.dto.User;
import com.wirehall.commandbuilder.dto.auth.SignUp;
import com.wirehall.commandbuilder.exception.BadRequestException;
import com.wirehall.commandbuilder.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(SignUp signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }
        return userRepository.addUser(signUpRequest);
    }

    public Optional<User> findById(Object id) {
        return userRepository.findById(id);
    }
}
