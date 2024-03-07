package com.example.backend1640.service.impl;

import com.example.backend1640.dto.CreateUserDTO;
import com.example.backend1640.dto.UserDTO;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(CreateUserDTO userDTO) {
        validateUserExists(userDTO.getEmail());
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());

        //Save User
        User savedUser = userRepository.save(user);
        UserDTO responseUserDTO = new UserDTO();
        BeanUtils.copyProperties(savedUser, responseUserDTO );

        return responseUserDTO;
    }


    private void validateUserExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("UserAlreadyExists");
        }
    }
}
