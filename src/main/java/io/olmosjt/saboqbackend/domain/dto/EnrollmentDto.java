package io.olmosjt.saboqbackend.domain.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnrollmentDto {
    public record Response(
            CourseDto.Summary course,
            Float progress,
            LocalDateTime lastAccessed
    ) {}

    public record UpdateProgressRequest(
            Float progress // 0.0 to 1.0 (or 0 to 100 depending on your preference, assuming 0.0-100.0)
    ) {}
}
