package com.example.todo.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class TechnicalStackPatchRequest {
    @NotNull
    private List<String> skills;

    public TechnicalStackPatchRequest() {}

    public TechnicalStackPatchRequest(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}