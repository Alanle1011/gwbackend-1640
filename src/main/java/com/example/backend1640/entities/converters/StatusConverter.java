package com.example.backend1640.entities.converters;

import com.example.backend1640.constants.StatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusConverter implements AttributeConverter<StatusEnum, String>{

    @Override
    public String convertToDatabaseColumn(StatusEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString(); // Convert Enum to String
    }

    @Override
    public StatusEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return StatusEnum.valueOf(dbData); // Convert String to Enum
    }

}
