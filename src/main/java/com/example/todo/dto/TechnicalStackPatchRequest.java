// src/main/java/com/example/todo/dto/TechnicalStackPatchRequest.java
package com.example.todo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record TechnicalStackPatchRequest(
        @NotNull
        @Size(max = 50, message = "Maximum 50 skills authorized")
        List<@NotBlank(message = "Skills can't be blank")
        @Size(max = 30, message = "One skill could not contains more than 30 characters") String> skills
) {}
