package com.example.backend1640.dto;

import com.example.backend1640.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFacultyDTO {
    private String faculty_name;
    private User manager_id;
}
