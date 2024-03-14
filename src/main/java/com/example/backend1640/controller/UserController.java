package com.example.backend1640.controller;

import com.example.backend1640.dto.CreateUserDTO;
import com.example.backend1640.dto.LoginDTO;
import com.example.backend1640.dto.LoginRequestDTO;
import com.example.backend1640.dto.UserDTO;
import com.example.backend1640.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDTO createUser(@Validated @RequestBody CreateUserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @PostMapping(path = "/login")
    public LoginDTO loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        return userService.loginUser(loginRequestDTO);
    }
}
