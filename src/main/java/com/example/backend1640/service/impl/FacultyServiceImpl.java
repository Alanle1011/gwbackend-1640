package com.example.backend1640.service.impl;

import com.example.backend1640.dto.CreateFacultyDTO;
import com.example.backend1640.dto.FacultyDTO;
import com.example.backend1640.entity.Faculty;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.FacultyAlreadyExistsException;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.exception.UserNotExistsException;
import com.example.backend1640.repository.FacultyRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.FacultyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    private final UserRepository userRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository, UserRepository userRepository) {
        this.facultyRepository = facultyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FacultyDTO createFaculty(CreateFacultyDTO facultyDTO) {
        validateFacultyExists(facultyDTO.getFacultyName());
        Faculty faculty = new Faculty();

        User managerId = validateUserExists(facultyDTO.getManagerId());

        BeanUtils.copyProperties(facultyDTO, faculty);
        faculty.setCreatedAt(new Date());
        faculty.setUpdatedAt(new Date());
        faculty.setManagerId(managerId);

        //Save Faculty
        Faculty savedFaculty = facultyRepository.save(faculty);
        FacultyDTO responseFacultyDTO = new FacultyDTO();
        BeanUtils.copyProperties(savedFaculty, responseFacultyDTO);

        return responseFacultyDTO;
    }

    private void validateFacultyExists(String facultyName) {
        Optional<Faculty> optionalFaculty = facultyRepository.findByFacultyName(facultyName);

        if (optionalFaculty.isPresent()) {
            throw new FacultyAlreadyExistsException("FacultyAlreadyExists");
        }
    }

    private User validateUserExists(Long managerId) {
        Optional<User> optionalUser = userRepository.findById(managerId);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("UserNotExists");
        }
        return optionalUser.get();
    }
}
