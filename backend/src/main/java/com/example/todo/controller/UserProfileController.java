package com.example.todo.controller;

import com.example.todo.UserProfile;
import com.example.todo.dto.ErrorResponse;
import com.example.todo.dto.TechnicalStackPatchRequest;
import com.example.todo.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
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

    @PatchMapping(
            value = "/profiles/{id}/technical-stack",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update only a user technical stack")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Technical stack updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserProfile.class))),
            @ApiResponse(responseCode = "400", description = "Payload invalid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserProfile> patchUserTechnicalStack(
            @PathVariable Long id,
            @Valid @RequestBody TechnicalStackPatchRequest body) {

        UserProfile updated = userProfileService.updateTechnicalStack(id, body.skills());
        return ResponseEntity.ok(updated);
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
}