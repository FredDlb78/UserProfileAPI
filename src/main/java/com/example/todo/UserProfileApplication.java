package com.example.todo;

import com.example.todo.repository.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserProfileApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserProfileApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserProfileRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				repository.save(new UserProfile(
						null,
						"Fredéric",
						"Delabre",
						"QA Automation Engineer",
						5,
						"Java/Selenium/Appium, backend & IA."
				));
				System.out.println("Profiled inserted!");
			}
			repository.findAll().forEach(
					profile -> System.out.println("Profile found : " + profile.getFullName())
			);
		};
	}
}
