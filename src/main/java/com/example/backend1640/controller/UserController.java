package com.example.backend1640.controller;

import com.example.backend1640.dto.*;
import com.example.backend1640.entity.User;
import com.example.backend1640.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDTO createUser(@Validated @RequestBody CreateUserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping(path = "/login")
    public LoginDTO loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.loginUser(loginRequestDTO);
    }

    @GetMapping
    public List<ReadUserDTO> getAllUser() {
        return userService.findAll();
    }

    @PutMapping("/update/{id}")
    public UserDTO updateUser(@PathVariable long id, @Validated @RequestBody UpdateUserDTO userDTO) {
        userDTO.setId(id);
        return userService.updateUser(userDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
