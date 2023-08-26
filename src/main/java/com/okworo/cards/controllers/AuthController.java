package com.okworo.cards.controllers;

import com.okworo.cards.enums.Role;
import com.okworo.cards.dao.request.SignInRequest;
import com.okworo.cards.dao.request.UserRequest;
import com.okworo.cards.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Morris.Okworo on 26/08/2023
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("signUp")
    public ResponseEntity<?> signUp(@RequestBody UserRequest signUpModel) {
        return ResponseEntity.ok(userService.addUser(signUpModel, Role.MEMBER));

    }

    @PostMapping("signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInModel, HttpServletResponse response) {
        return ResponseEntity.ok(userService.signIn(signInModel, response));
    }
}
