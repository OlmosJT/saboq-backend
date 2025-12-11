package io.olmosjt.saboqbackend.application.controller;

import io.olmosjt.saboqbackend.domain.dto.AuthDtos;
import io.olmosjt.saboqbackend.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthDtos.AuthResponse> register(
            @RequestBody AuthDtos.RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponse> authenticate(
            @RequestBody AuthDtos.LoginRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
