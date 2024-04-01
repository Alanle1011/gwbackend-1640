package com.example.backend1640.service;

import com.example.backend1640.dto.CreateUserDTO;
import com.example.backend1640.dto.LoginDTO;
import com.example.backend1640.dto.LoginRequestDTO;
import com.example.backend1640.dto.ReadUserDTO;
import com.example.backend1640.dto.UpdateUserDTO;
import com.example.backend1640.dto.UserDTO;

import java.util.List;


public interface UserService {

    UserDTO createUser(CreateUserDTO userDTO);

    LoginDTO loginUser(LoginRequestDTO loginRequestDTO);

    List<ReadUserDTO> findAll();

    UserDTO updateUser(UpdateUserDTO userDTO);

    void deleteUser(long id);
}
