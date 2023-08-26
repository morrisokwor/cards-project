package com.okworo.cards;

import com.okworo.cards.entities.UserEntity;
import com.okworo.cards.enums.Role;
import com.okworo.cards.dao.request.UserRequest;
import com.okworo.cards.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
@RequiredArgsConstructor
public class CardsApplication {

    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }


    @PostConstruct
    public void init() {
        Optional<UserEntity> user = userService.findByEmail("user@gmail.com");
        Optional<UserEntity> admin = userService.findByEmail("admin@gmail.com");

        if (!user.isPresent()) {
            userService.addUser(UserRequest.builder()
                    .email("user@gmail.com")
                    .firstName("Morris")
                    .lastName("Okworo")
                    .password("abc123")
                    .build(), Role.MEMBER);
        }

        if (!admin.isPresent()) {
            userService.addUser(UserRequest.builder()
                    .email("admin@gmail.com")
                    .firstName("Admin")
                    .lastName("Admin")
                    .password("abc123")
                    .build(), Role.ADMIN);
        }
    }


}
