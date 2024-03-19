package com.example.backend1640.service.impl;

import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.CreateUserDTO;
import com.example.backend1640.dto.LoginDTO;
import com.example.backend1640.dto.LoginRequestDTO;
import com.example.backend1640.dto.UserDTO;
import com.example.backend1640.entity.Faculty;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.FacultyNotExistsException;
import com.example.backend1640.exception.MissingStudentFacultyException;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.exception.UserNotExistsException;
import com.example.backend1640.repository.FacultyRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, FacultyRepository facultyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(CreateUserDTO userDTO) {
        if(userDTO.getUserRole() == UserRoleEnum.STUDENT && userDTO.getFaculty() == null) {
            throw new MissingStudentFacultyException("Student must have a faculty");
        }


        validateUserExists(userDTO.getEmail());
        User user = new User();
        String encodePassword = passwordEncoder.encode(userDTO.getPassword());
        if(userDTO.getUserRole() == UserRoleEnum.STUDENT){
            Faculty faculty = validateFacultyExists(userDTO.getFaculty());
            user.setFacultyId(faculty);
        }
        BeanUtils.copyProperties(userDTO, user);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setPassword(encodePassword);

        //Save User
        User savedUser = userRepository.save(user);
        UserDTO responseUserDTO = new UserDTO();
        BeanUtils.copyProperties(savedUser, responseUserDTO );
        if(savedUser.getUserRole() == UserRoleEnum.STUDENT){
            responseUserDTO.setFaculty(savedUser.getFacultyId().getFacultyName());
        }

        return responseUserDTO;
    }

    @Override
    public LoginDTO loginUser(LoginRequestDTO loginRequestDTO) {
        User user = validateUserNotExists(loginRequestDTO.getEmail());
        Boolean isCorrectPassword = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

        if (!isCorrectPassword) {
            throw new UserNotExistsException("Wrong password");
        }

        Optional<User> optionalUser = userRepository.findOneByEmailAndPassword(user.getEmail(), user.getPassword());

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("Login Fail");
        }

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setMessage("Correct password");
        loginDTO.setStatus(true);
        loginDTO.setRole(optionalUser.get().getUserRole());
        loginDTO.setUserId(optionalUser.get().getId());

        return loginDTO;

    }

    private Faculty validateFacultyExists(Long id) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);

        if (optionalFaculty.isEmpty()) {
            throw new FacultyNotExistsException("Faculty does not exist");
        }
        return optionalFaculty.get();
    }


    private void validateUserExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("UserAlreadyExists");
        }
    }

    private User validateUserNotExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("User Not Exists");
        }

        return optionalUser.get();
    }
}
