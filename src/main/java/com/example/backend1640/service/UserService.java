package com.example.backend1640.service;

import com.example.backend1640.dto.*;

import java.util.List;


public interface UserService {

    UserDTO createUser(CreateUserDTO userDTO);

    LoginDTO loginUser(LoginRequestDTO loginRequestDTO);

    List<ReadUserDTO> findAll();

    ReadUserByIdDTO findById(long id);

    UserDTO updateUser(UpdateUserDTO userDTO);

    void deleteUser(long id);
}
