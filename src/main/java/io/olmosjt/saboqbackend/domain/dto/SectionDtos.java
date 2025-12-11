package io.olmosjt.saboqbackend.domain.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SectionDtos {

    // Used inside SaveLessonRequest
    public record SectionDto(
            UUID id, // Null = Create New, UUID = Update Existing
            String title,
            Integer position,
            List<ComponentDtos.ComponentDto> components
    ) {}

    // Used inside LessonDetailResponse
    public record SectionResponse(
            UUID id,
            String title,
            Integer position,
            List<ComponentDtos.ComponentResponse> components
    ) {}

}
