package com.example.backend1640.config;

import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ConverterConfig {
    @Bean
    public IConverter localConverter() {
        return LocalConverter.builder().build();
    }
}
