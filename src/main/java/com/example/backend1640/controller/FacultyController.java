package com.example.backend1640.controller;

import com.example.backend1640.dto.CreateFacultyDTO;
import com.example.backend1640.dto.FacultyDTO;
import com.example.backend1640.dto.ReadFacultyDTO;
import com.example.backend1640.dto.UpdateFacultyDTO;
import com.example.backend1640.service.FacultyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<ReadFacultyDTO> getAllFaculty() {
        return facultyService.findAll();
    }

    @PutMapping("/update/{id}")
    public FacultyDTO updateFaculty(@PathVariable long id, @Validated @RequestBody UpdateFacultyDTO facultyDTO) {
        facultyDTO.setId(id);
        return facultyService.updateFaculty(facultyDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
    }
}
