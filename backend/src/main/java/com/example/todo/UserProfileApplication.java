package com.example.todo;

import com.example.todo.repository.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class UserProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserProfileApplication.class, args);
    }

    /**
     * Seed de données uniquement en profil "local".
     * Lance l’app avec: -Dspring-boot.run.profiles=local
     */
    @Bean
    @Profile("local")
    CommandLineRunner demo(UserProfileRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                UserProfile p = new UserProfile();
                p.setFullName("Fredéric");
                p.setTitle("Delabre");
                p.setDescription("QA Automation Engineer");
                p.setExperienceYears(5);
                p.setTechnicalStack(new ArrayList<>(List.of("Java", "Selenium", "Appium", "Postman")));
                repository.save(p);
                System.out.println("Profile inserted!");
            }
            repository.findAll().forEach(
                    profile -> System.out.println("Profile found : " + profile.getFullName())
            );
        };
    }
}
