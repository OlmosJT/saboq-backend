package io.olmosjt.saboqbackend.domain.service;

import io.olmosjt.saboqbackend.domain.dto.AuthDtos;
import io.olmosjt.saboqbackend.domain.entity.User;
import io.olmosjt.saboqbackend.domain.repository.UserRepository;
import io.olmosjt.saboqbackend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        var user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .username(request.email()) // Default username to email for simplicity
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        // Convert to Spring UserDetails for token generation
        var userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPasswordHash(), java.util.Collections.emptyList()
        );
        var jwtToken = jwtService.generateToken(userDetails);

        return new AuthDtos.AuthResponse(jwtToken);
    }

    public AuthDtos.AuthResponse authenticate(AuthDtos.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        var user = userRepository.findByEmail(request.email()).orElseThrow();

        var userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPasswordHash(), java.util.Collections.emptyList()
        );
        var jwtToken = jwtService.generateToken(userDetails);

        return new AuthDtos.AuthResponse(jwtToken);
    }
}
