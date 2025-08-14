package com.example.todo.service;

import com.example.todo.UserProfile;
import com.example.todo.exception.ProfileNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProfileService {

    private final List<UserProfile> profiles = new ArrayList<>(List.of(
            new UserProfile(1L, "Frédéric Delabre", "QA Auto", "QA Auto Selenium/Appium Java", 5, "Selenium - Appium - Java - TestNG - JUnit - Cucumber - Jenkins - Gitlab CI - Github Actions - Cypress - JavaScript - Playwright - Postman - Swagger"),
            new UserProfile(2L, "James Habricot", "QA Auto", "QA Auto Selenium Java", 5, "Selenium - Java - JUnit - Cucumber - Cypress - JavaScript - Playwright"),
            new UserProfile(3L, "Kemal Tabbech", "QA Lead & Auto", "QA Lead & Auto Cypress JS", 5, "Cypress - JavaScript - Mocha - Postman - Cypress - Swagger - Playwright"),
            new UserProfile(4L, "Stéphane Le Mauff", "QA Auto", "QA Auto Playwright JS/TS", 5, "Playwright - JavaScript - TypeScript - Postman"),
            new UserProfile(5L, "Imam Lahmouid", "QA Auto", "QA Auto Playwright JS", 5, "Playwright - JavaScript - Postman")
            ));

    private Long nextId = 6L;

    public List<UserProfile> getAllProfiles() {
        return profiles;
    }

    public UserProfile getProfileById(Long id) {
        return profiles.stream()
                .filter(profile -> profile.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }

    public UserProfile createProfile(UserProfile profile) {
        profile.setId(nextId++);
        profiles.add(profile);
        return profile;
    }

    public UserProfile updateProfile(Long id, UserProfile updatedProfile) {
        UserProfile profile = getProfileById(id);
        profile.setFullName(updatedProfile.getFullName());
        profile.setTitle(updatedProfile.getTitle());
        profile.setDescription(updatedProfile.getDescription());
        profile.setExperienceYears(updatedProfile.getExperienceYears());
        profile.setTechnicalStack(profile.getTechnicalStack());
        return profile;
    }

    public void deleteProfile(Long id) {
        boolean removed = profiles.removeIf(profile -> profile.getId().equals(id));
        if (!removed) throw new ProfileNotFoundException(id);
    }
}