package io.olmosjt.saboqbackend.domain.mapper;

import io.olmosjt.saboqbackend.domain.dto.ComponentDto;
import io.olmosjt.saboqbackend.domain.dto.LessonDto;
import io.olmosjt.saboqbackend.domain.dto.SectionDto;
import io.olmosjt.saboqbackend.domain.entity.Component;
import io.olmosjt.saboqbackend.domain.entity.Lesson;
import io.olmosjt.saboqbackend.domain.entity.Section;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class LessonMapper {

    // --- 1. Summary ---
    public static LessonDto.Summary toSummary(Lesson lesson) {
        return new LessonDto.Summary(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getPosition(),
                lesson.isPublished(),
                lesson.getStatus()
        );
    }

    // --- 2. Detail (Deep Map) ---
    public static LessonDto.Detail toDetail(Lesson lesson) {
        return new LessonDto.Detail(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getPosition(),
                lesson.isPublished(),
                lesson.getStatus(),
                toSectionDetailList(lesson.getSections())
        );
    }

    // --- Helpers ---

    private static List<SectionDto.Detail> toSectionDetailList(List<Section> sections) {
        if (sections == null) return Collections.emptyList();
        return sections.stream().map(LessonMapper::toSectionDetail).toList();
    }

    private static SectionDto.Detail toSectionDetail(Section section) {
        return new SectionDto.Detail(
                section.getId(),
                section.getTitle(),
                section.getPosition(),
                toComponentDetailList(section.getComponents())
        );
    }

    private static List<ComponentDto.Detail> toComponentDetailList(List<Component> components) {
        if (components == null) return Collections.emptyList();
        return components.stream().map(LessonMapper::toComponentDetail).toList();
    }

    private static ComponentDto.Detail toComponentDetail(Component component) {
        return new ComponentDto.Detail(
                component.getId(),
                component.getType(),
                component.getContent(),
                component.getPosition()
        );
    }
}
