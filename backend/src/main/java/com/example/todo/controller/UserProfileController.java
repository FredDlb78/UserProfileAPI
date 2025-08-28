package com.example.todo.controller;

import com.example.todo.UserProfile;
import com.example.todo.dto.ErrorResponse;
import com.example.todo.dto.TechnicalStackPatchRequest;
import com.example.todo.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/profiles")
    @Operation(summary = "Get all profiles")
    @ApiResponse(responseCode = "200", description = "List of all profiles",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)))
    public List<UserProfile> getAllProfiles() {
        return userProfileService.getAllProfiles();
    }

    @GetMapping("/profiles/{id}")
    @Operation(summary = "Get a single profile by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile successfully found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserProfile> getSingleProfile(@PathVariable Long id) {
        if (id <= 0) throw new IllegalArgumentException("ID must be greater than 0");
        return ResponseEntity.ok(userProfileService.getProfileById(id));
    }

    @PostMapping("/profiles")
    @Operation(summary = "Create a profile")
    @ApiResponse(responseCode = "201", description = "Profile successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)))
    public ResponseEntity<UserProfile> createProfile(@RequestBody UserProfile newProfile) {
        UserProfile created = userProfileService.createProfile(newProfile);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/profiles/{id}")
    @Operation(summary = "Update a profile by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserProfile> updateProfile(@PathVariable Long id, @RequestBody UserProfile updatedProfile) {
        UserProfile profile = userProfileService.updateProfile(id, updatedProfile);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/profiles/{id}")
    @Operation(summary = "Delete a profile by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        userProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    /* ====== NEW: PATCH skills only ====== */
    @PatchMapping("/profiles/{id}/technical-stack")
    @Operation(summary = "Update an user's technical stack")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technical stack successfully patched",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserProfile> patchUserTechnicalStack(
            @PathVariable Long id,
            @RequestBody @Valid TechnicalStackPatchRequest body
    ) {
        UserProfile updated = userProfileService.updateTechnicalStack(id, body.getSkills());
        return ResponseEntity.ok(updated);
    }

}
