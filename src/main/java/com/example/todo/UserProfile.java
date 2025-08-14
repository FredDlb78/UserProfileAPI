package com.example.todo;

public class UserProfile {
    private Long id;
    private String fullName;
    private String title;
    private String description;
    private Integer experienceYears;
    private String technicalStack;

    public UserProfile(Long id,String fullName, String title, String description, Integer experienceYears, String technicalStack) {
        this.id = id;
        this.fullName = fullName;
        this.title = title;
        this.description = description;
        this.experienceYears = experienceYears;
        this.technicalStack = technicalStack;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getTechnicalStack() {
        return technicalStack;
    }

    public void setTechnicalStack(String technicalStack) {
        this.technicalStack = technicalStack;
    }

}