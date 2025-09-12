package com.univ.attendograde;

import com.univ.attendograde.console.ConsoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AttendogradeApplication implements CommandLineRunner {

    private final ConsoleService consoleService;

    public AttendogradeApplication(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    public static void main(String[] args) {
        SpringApplication.run(AttendogradeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        consoleService.start();
    }
}
