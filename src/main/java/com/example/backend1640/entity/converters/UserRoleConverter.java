package com.example.backend1640.entity.converters;

import com.example.backend1640.constants.UserRoleEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserRoleConverter implements AttributeConverter<UserRoleEnum, String>{
    @Override
    public String convertToDatabaseColumn(UserRoleEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString(); // Convert Enum to String
    }

    @Override
    public UserRoleEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return UserRoleEnum.valueOf(dbData); // Convert String to Enum
    }
}
