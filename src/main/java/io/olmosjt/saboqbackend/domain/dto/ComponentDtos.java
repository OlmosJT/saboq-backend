package io.olmosjt.saboqbackend.domain.dto;

import io.olmosjt.saboqbackend.domain.ComponentContent;
import io.olmosjt.saboqbackend.domain.enums.ComponentType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComponentDtos {

    // Used inside SaveLessonRequest
    public record ComponentDto(
            UUID id, // Null = Create New, UUID = Update Existing
            ComponentContent content, // Polymorphic JSON
            Integer position
    ) {}

    // Used inside LessonDetailResponse
    public record ComponentResponse(
            UUID id,
            ComponentType type,
            ComponentContent content,
            Integer position
    ) {}
}
