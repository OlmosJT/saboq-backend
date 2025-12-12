package io.olmosjt.saboqbackend.domain.dto;

import io.olmosjt.saboqbackend.domain.enums.LessonStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LessonDto {

    public record UpsertRequest(
            String title,
            Integer position,
            Boolean isPublished,
            LessonStatus status,
            List<SectionDto.Upsert> sections // Renamed from SectionDtos.SectionDto
    ) {}

    public record Summary(
            UUID id,
            String title,
            Integer position,
            Boolean isPublished,
            LessonStatus status
    ) {}

    public record Detail(
            UUID id,
            String title,
            Integer position,
            Boolean isPublished,
            LessonStatus status,
            List<SectionDto.Detail> sections // Renamed from SectionDtos.SectionResponse
    ) {}
}
