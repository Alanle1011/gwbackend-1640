package com.example.backend1640.service.impl;

import com.example.backend1640.dto.CreateFacultyDTO;
import com.example.backend1640.dto.FacultyDTO;
import com.example.backend1640.dto.ReadFacultyDTO;
import com.example.backend1640.dto.UpdateFacultyDTO;
import com.example.backend1640.entity.Faculty;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.FacultyAlreadyExistsException;
import com.example.backend1640.exception.FacultyNotExistsException;
import com.example.backend1640.exception.UserAlreadyExistsException;
import com.example.backend1640.exception.UserNotExistsException;
import com.example.backend1640.repository.FacultyRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.FacultyService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        User coordinatorId = validateUserExists(facultyDTO.getCoordinatorId());

        BeanUtils.copyProperties(facultyDTO, faculty);
        faculty.setCreatedAt(new Date());
        faculty.setUpdatedAt(new Date());
        faculty.setCoordinatorId(coordinatorId);

        //Save Faculty
        Faculty savedFaculty = facultyRepository.save(faculty);
        FacultyDTO responseFacultyDTO = new FacultyDTO();
        BeanUtils.copyProperties(savedFaculty, responseFacultyDTO);

        return responseFacultyDTO;
    }

    @Override
    public List<ReadFacultyDTO> findAll() {
        List<Faculty> faculties = facultyRepository.findAll();
        List<ReadFacultyDTO> readFacultyDTOs = new ArrayList<>();

        for (Faculty faculty : faculties) {
            ReadFacultyDTO readFacultyDTO = new ReadFacultyDTO();
            BeanUtils.copyProperties(faculty, readFacultyDTO);

            readFacultyDTO.setCoordinatorId(faculty.getCoordinatorId().getId().toString() + " - " + faculty.getCoordinatorId().getName());
            readFacultyDTOs.add(readFacultyDTO);
        }
        return readFacultyDTOs;
    }

    @Override
    public FacultyDTO updateFaculty(UpdateFacultyDTO facultyDTO) {
        Faculty faculty = validateFacultyExists(facultyDTO.getId());

        if (facultyDTO.getCoordinatorId() != null) {
            User coordinatorId = validateUserExists(facultyDTO.getCoordinatorId());
            faculty.setCoordinatorId(coordinatorId);
        }
        if (facultyDTO.getFacultyName() != null){
            faculty.setFacultyName(facultyDTO.getFacultyName());
        }
        faculty.setUpdatedAt(new Date());

        Faculty savedFaculty = facultyRepository.save(faculty);
        FacultyDTO responseFacultyDTO = new FacultyDTO();
        BeanUtils.copyProperties(savedFaculty, responseFacultyDTO);
        responseFacultyDTO.setCoordinatorId(savedFaculty.getCoordinatorId().getId());

        return responseFacultyDTO;
    }

    @Override
    public void deleteFaculty(long id) {
        Faculty faculty = validateFacultyExists(id);
        facultyRepository.delete(faculty);
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

    private Faculty validateFacultyExists(Long id) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);

        if (optionalFaculty.isEmpty()) {
            throw new FacultyNotExistsException("Faculty does not exist");
        }
        return optionalFaculty.get();
    }
}
