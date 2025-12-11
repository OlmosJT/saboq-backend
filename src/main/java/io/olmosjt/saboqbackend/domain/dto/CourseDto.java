package io.olmosjt.saboqbackend.domain.dto;

import io.olmosjt.saboqbackend.domain.enums.CourseStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CourseDto {
    public record CreateCourseRequest(
            String title,
            String description,
            String thumbnailUrl
    ) {}

    public record UpdateCourseRequest(
            String title,
            String description,
            String thumbnailUrl,
            Boolean isPublished
    ) {}

    public record CourseSummaryResponse(
            UUID id,
            String title,
            String description,
            String thumbnailUrl,
            CourseStatus status,
            Boolean isPublished,
            UUID authorId,
            LocalDateTime createdAt
    ) {}

    public record CourseDetailResponse(
            UUID id,
            String title,
            String description,
            String thumbnailUrl,
            CourseStatus status,
            Boolean isPublished,
            UUID authorId,
            java.util.List<LessonDtos.LessonSummaryResponse> lessons,
            LocalDateTime createdAt
    ) {}


}
