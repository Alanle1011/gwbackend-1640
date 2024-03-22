package com.example.backend1640.service;

import com.example.backend1640.dto.CreateFacultyDTO;
import com.example.backend1640.dto.FacultyDTO;
import com.example.backend1640.dto.ReadFacultyDTO;
import com.example.backend1640.dto.UpdateFacultyDTO;

import java.util.List;


public interface FacultyService {
    FacultyDTO createFaculty(CreateFacultyDTO facultyDTO);

    List<ReadFacultyDTO> findAll();

    FacultyDTO updateFaculty(UpdateFacultyDTO facultyDTO);

    void deleteFaculty(long id);
}
