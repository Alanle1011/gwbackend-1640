package com.example.backend1640.service;

import com.example.backend1640.dto.CreateUserDTO;
import com.example.backend1640.dto.UserDTO;
import org.springframework.stereotype.Service;


public interface UserService {

    UserDTO createUser(CreateUserDTO userDTO);
}
