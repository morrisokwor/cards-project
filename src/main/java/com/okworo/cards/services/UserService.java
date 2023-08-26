package com.okworo.cards.services;

import com.okworo.cards.dao.request.SignInRequest;
import com.okworo.cards.dao.request.UserRequest;
import com.okworo.cards.dao.response.SignInResponse;
import com.okworo.cards.dao.response.UserResponse;
import com.okworo.cards.entities.UserEntity;
import com.okworo.cards.enums.Role;
import com.okworo.cards.exceptions.CustomValidationException;
import com.okworo.cards.repository.UserRepository;
import com.okworo.cards.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Morris.Okworo on 26/08/2023
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtProvider;


    public UserResponse addUser(UserRequest signUpModel, Role role) {

        if (userRepository.findByEmail(signUpModel.getEmail()).isPresent()) {
            throw new CustomValidationException("Email Address Already taken!");
        }

        UserEntity user = UserEntity.builder()
                .email(signUpModel.getEmail().toLowerCase())
                .firstName(signUpModel.getFirstName())
                .lastName(signUpModel.getLastName())
                .role(role)
                .password(passwordEncoder.encode(signUpModel.getPassword()))
                .build();

        UserEntity savedUser = userRepository.save(user);


        return UserResponse.builder()
                .email(savedUser.getEmail())
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .role(savedUser.getRole())
                .build();

    }

    public SignInResponse signIn(SignInRequest signInRequest, HttpServletResponse response) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            UserEntity user = userRepository.findByEmail(signInRequest.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            String jwt = jwtProvider.createAuthToken(user);

            Cookie cookie = new Cookie("x-refresh", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge(3 * 24 * 60 * 60);
            cookie.setPath("/");

            response.addCookie(cookie);

            return SignInResponse.builder()
                    .accessToken(jwt)
                    .message("Success")
                    .success(true)
                    .user(UserResponse.builder()
                            .id(user.getId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .build())
                    .build();
        } catch (LockedException e) {
            return SignInResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .build();
        } catch (Exception e) {
            return SignInResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
