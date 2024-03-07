package com.example.backend1640.service.impl;

import com.example.backend1640.dto.CreateFacultyDTO;
import com.example.backend1640.dto.FacultyDTO;
import com.example.backend1640.entity.Faculty;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.repository.FacultyRepository;
import com.example.backend1640.service.FacultyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public FacultyDTO createFaculty(CreateFacultyDTO facultyDTO) {
        validateFacultyExists(facultyDTO.getFaculty_name());
        Faculty faculty = new Faculty();
        BeanUtils.copyProperties(facultyDTO, faculty);
        faculty.setCreated_at(new Date());
        faculty.setUpdated_at(new Date());

        //Save Faculty
        Faculty savedFaculty = facultyRepository.save(faculty);
        FacultyDTO responseFacultyDTO = new FacultyDTO();
        BeanUtils.copyProperties(savedFaculty, responseFacultyDTO);

        return responseFacultyDTO;
    }

    private void validateFacultyExists(String facultyName) {
        Optional<Faculty> optionalFaculty = facultyRepository.findByName(facultyName);

        if (optionalFaculty.isPresent()) {
            throw new UserAlreadyExistsException("FacultyAlreadyExists");
        }
    }
}
