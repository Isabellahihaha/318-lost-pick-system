package com.lostandfound.aimatching.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExtractionRequest {
    @NotBlank(message = "Natural language description is required")
    private String description;
}