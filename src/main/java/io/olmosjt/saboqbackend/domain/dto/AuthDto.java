package io.olmosjt.saboqbackend.domain.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class AuthDto {
    public record RegisterRequest(String firstName, String lastName, String email, String password) {}
    public record LoginRequest(String email, String password) {}
    public record AuthResponse(String token) {}
}
