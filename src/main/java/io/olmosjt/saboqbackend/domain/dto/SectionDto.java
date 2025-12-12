package io.olmosjt.saboqbackend.domain.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SectionDto {

    public record Upsert(
            UUID id, // Null = Create, UUID = Update
            String title,
            Integer position,
            List<ComponentDto.Upsert> components
    ) {}

    // Used inside LessonDto.Detail
    public record Detail(
            UUID id,
            String title,
            Integer position,
            List<ComponentDto.Detail> components
    ) {}

}
