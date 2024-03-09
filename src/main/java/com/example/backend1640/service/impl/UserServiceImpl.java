package com.example.backend1640.service.impl;

import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.CreateUserDTO;
import com.example.backend1640.dto.UserDTO;
import com.example.backend1640.entity.Faculty;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.FacultyNotExistsException;
import com.example.backend1640.exception.MissingStudentFacultyException;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.repository.FacultyRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;

    public UserServiceImpl(UserRepository userRepository, FacultyRepository facultyRepository) {
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public UserDTO createUser(CreateUserDTO userDTO) {
        if(userDTO.getUserRole() == UserRoleEnum.STUDENT && userDTO.getFaculty() == null) {
            throw new MissingStudentFacultyException("Student must have a faculty");
        }


        validateUserExists(userDTO.getEmail());
        User user = new User();
        if(userDTO.getUserRole() == UserRoleEnum.STUDENT){
            Faculty faculty = validateFacultyExists(userDTO.getFaculty());
            user.setFacultyId(faculty);
        }
        BeanUtils.copyProperties(userDTO, user);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        //Save User
        User savedUser = userRepository.save(user);
        UserDTO responseUserDTO = new UserDTO();
        BeanUtils.copyProperties(savedUser, responseUserDTO );
        if(savedUser.getUserRole() == UserRoleEnum.STUDENT){
            responseUserDTO.setFaculty(savedUser.getFacultyId().getFacultyName());
        }

        return responseUserDTO;
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
}
