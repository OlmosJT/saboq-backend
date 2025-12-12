package io.olmosjt.saboqbackend.domain.dto;

import io.olmosjt.saboqbackend.domain.enums.CourseStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CourseDto {
    public record CreateRequest(
            String title,
            String description,
            String thumbnailUrl // Preview Image URL
    ) {}

    public record UpdateRequest(
            String title,
            String description,
            String thumbnailUrl,
            Boolean isPublished
    ) {}

    public record Summary(
            UUID id,
            AuthorInfo author,
            String title,
            String description,
            String thumbnailUrl,
            CourseStatus status,
            Boolean isPublished,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record Detail(
            UUID id,
            String title,
            String description,
            String thumbnailUrl,
            CourseStatus status,
            Boolean isPublished,
            AuthorInfo author,
            java.util.List<LessonDto.Summary> lessons,
            LocalDateTime createdAt
    ) {}

    public record AuthorInfo(
            UUID id,
            String email,
            String username,
            String firstName,
            String lastName,
            String avatarUrl
    ) {}

}
