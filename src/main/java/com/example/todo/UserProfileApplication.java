package com.example.todo;

import com.example.todo.repository.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class UserProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserProfileApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserProfileRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                UserProfile fred = new UserProfile(
                        null,
                        "Frédéric",
                        "QA Automation Engineer",
                        "Automatisation tests Java/Selenium/Appium",
                        5,
                        new ArrayList<>(List.of("Java", "Selenium", "Appium", "Postman", "Swagger"))
                );
                repository.save(fred);
                System.out.println("Profile inserted!");
            }
            repository.findAll().forEach(p ->
                    System.out.println("Profile found : " + p.getFullName())
            );
        };
    }
}