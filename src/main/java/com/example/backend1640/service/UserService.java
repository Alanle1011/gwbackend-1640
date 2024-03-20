package com.example.backend1640.service;

import com.example.backend1640.dto.*;
import com.example.backend1640.entity.User;

import java.util.List;


public interface UserService {

    UserDTO createUser(CreateUserDTO userDTO);

    LoginDTO loginUser(LoginRequestDTO loginRequestDTO);

    List<ReadUserDTO> findAll();

    UserDTO updateUser(UpdateUserDTO userDTO);
}
