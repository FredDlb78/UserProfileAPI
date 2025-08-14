package com.example.todo.exception;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(Long id) {
        super("Profile with ID " + id + " not found.");
    }
}