package io.olmosjt.saboqbackend.domain.dto;

import io.olmosjt.saboqbackend.domain.enums.LessonStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LessonDtos {

    // --- WRITE (Input from Frontend) ---
    // Handles BOTH "New Lesson" (Create) and "Edit Lesson" (Update)
    public record SaveLessonRequest(
            String title,
            Integer position,
            boolean isPublished,
            List<SectionDtos.SectionDto> sections // The full hierarchy
    ) {}

    // --- READ (Output to Frontend) ---

    // For the Course Table of Contents
    public record LessonSummaryResponse(
            UUID id,
            String title,
            Integer position,
            boolean isPublished,
            LessonStatus status
    ) {}

    // For the Lesson Editor/Viewer
    public record LessonDetailResponse(
            UUID id,
            String title,
            Integer position,
            boolean isPublished,
            LessonStatus status,
            List<SectionDtos.SectionResponse> sections
    ) {}


}
