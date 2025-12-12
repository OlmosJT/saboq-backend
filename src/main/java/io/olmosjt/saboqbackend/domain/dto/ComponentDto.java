package io.olmosjt.saboqbackend.domain.dto;

import io.olmosjt.saboqbackend.domain.ComponentContent;
import io.olmosjt.saboqbackend.domain.enums.ComponentType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComponentDto {

    public record Upsert(
            UUID id, // Null = Create, UUID = Update
            ComponentContent content, // Polymorphic JSON
            Integer position
    ) {}

    // Used inside SectionDto.Detail
    public record Detail(
            UUID id,
            ComponentType type,
            ComponentContent content,
            Integer position
    ) {}
}
