package com.example.backend1640.service;

import com.example.backend1640.dto.CreateFacultyDTO;
import com.example.backend1640.dto.FacultyDTO;


public interface FacultyService {
    FacultyDTO createFaculty(CreateFacultyDTO facultyDTO);
}
