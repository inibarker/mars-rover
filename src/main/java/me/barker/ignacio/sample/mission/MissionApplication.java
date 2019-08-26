package me.barker.ignacio.sample.mission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MissionApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MissionApplication.class, args);
    }

}
