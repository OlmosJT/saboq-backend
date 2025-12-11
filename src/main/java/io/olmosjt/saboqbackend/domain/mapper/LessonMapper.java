package io.olmosjt.saboqbackend.domain.mapper;

import io.olmosjt.saboqbackend.domain.dto.ComponentDtos;
import io.olmosjt.saboqbackend.domain.dto.LessonDtos;
import io.olmosjt.saboqbackend.domain.dto.SectionDtos;
import io.olmosjt.saboqbackend.domain.entity.Lesson;
import io.olmosjt.saboqbackend.domain.entity.Section;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class LessonMapper {
    // --- 1. Map Single Lesson to Summary ---
    public LessonDtos.LessonSummaryResponse toSummary(Lesson lesson) {
        return new LessonDtos.LessonSummaryResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getPosition(),
                lesson.isPublished(),
                lesson.getStatus()
        );
    }

    // --- 2. Map Full Lesson Tree (Detail) ---
    public LessonDtos.LessonDetailResponse toDetail(Lesson lesson) {
        return new LessonDtos.LessonDetailResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getPosition(),
                lesson.isPublished(),
                lesson.getStatus(),
                toSectionResponseList(lesson.getSections())
        );
    }

    // --- Helper: Sections ---
    private List<SectionDtos.SectionResponse> toSectionResponseList(List<Section> sections) {
        if (sections == null) return Collections.emptyList();

        return sections.stream()
                .map(this::toSectionResponse)
                .toList();
    }

    private SectionDtos.SectionResponse toSectionResponse(Section section) {
        return new SectionDtos.SectionResponse(
                section.getId(),
                section.getTitle(),
                section.getPosition(),
                toComponentResponseList(section.getComponents())
        );
    }

    // --- Helper: Components ---
    private List<ComponentDtos.ComponentResponse> toComponentResponseList(List<io.olmosjt.saboqbackend.domain.entity.Component> components) {
        if (components == null) return Collections.emptyList();

        return components.stream()
                .map(this::toComponentResponse)
                .toList();
    }

    private ComponentDtos.ComponentResponse toComponentResponse(io.olmosjt.saboqbackend.domain.entity.Component component) {
        return new ComponentDtos.ComponentResponse(
                component.getId(),
                component.getType(),
                component.getContent(), // The JSONB object is passed as-is
                component.getPosition()
        );
    }
}
