package com.example.backend1640.controller;

import com.example.backend1640.dto.CreateFacultyDTO;
import com.example.backend1640.dto.FacultyDTO;
import com.example.backend1640.service.FacultyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public FacultyDTO createFaculty(@Validated @RequestBody CreateFacultyDTO facultyDTO){
        return facultyService.createFaculty(facultyDTO);
    }
}
