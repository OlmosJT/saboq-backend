package io.olmosjt.saboqbackend.domain.service;

import io.olmosjt.saboqbackend.domain.dto.ComponentDto;
import io.olmosjt.saboqbackend.domain.dto.LessonDto;
import io.olmosjt.saboqbackend.domain.dto.SectionDto;
import io.olmosjt.saboqbackend.domain.entity.Component;
import io.olmosjt.saboqbackend.domain.entity.Course;
import io.olmosjt.saboqbackend.domain.entity.Lesson;
import io.olmosjt.saboqbackend.domain.entity.Section;
import io.olmosjt.saboqbackend.domain.enums.LessonStatus;
import io.olmosjt.saboqbackend.domain.mapper.LessonMapper;
import io.olmosjt.saboqbackend.domain.repository.CourseRepository;
import io.olmosjt.saboqbackend.domain.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    // --- CREATE ---

    @Transactional
    public UUID createLesson(UUID userId, UUID courseId, LessonDto.UpsertRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // SECURITY CHECK: Is the actor the author of the course?
        validateAuthor(course, userId);

        Lesson lesson = Lesson.builder()
                .course(course)
                .title(request.title())
                .position(request.position() != null ? request.position() : 0)
                .isPublished(request.isPublished())
                .status(request.isPublished() ? LessonStatus.PUBLISHED : LessonStatus.DRAFT)
                .build();

        // Use the shared helper to handle deep structure
        mergeSections(lesson, request.sections());

        return lessonRepository.save(lesson).getId();
    }

    // --- UPDATE ---

    @Transactional
    public UUID updateLesson(UUID userId, UUID lessonId, LessonDto.UpsertRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // SECURITY CHECK: Is the actor the author of the parent course?
        validateAuthor(lesson.getCourse(), userId);

        // Update Fields
        if(request.title() != null && !request.title().isBlank()) {
            lesson.setTitle(request.title());
        }

        if (request.position() != null) {
            lesson.setPosition(request.position());
        }

        if(request.isPublished() != null) {
            lesson.setPublished(request.isPublished());
        }

        lesson.setStatus(request.status().equals(LessonStatus.PUBLISHED) ? LessonStatus.PUBLISHED : LessonStatus.DRAFT);

        mergeSections(lesson, request.sections());
        return lessonRepository.save(lesson).getId();
    }

    // --- READ ---

    @Transactional(readOnly = true)
    public LessonDto.Detail getLesson(UUID lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        return LessonMapper.toDetail(lesson);
    }

    // --- PUBLISH SHORTCUT ---

    @Transactional
    public void publishLesson(UUID userId, UUID lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        validateAuthor(lesson.getCourse(), userId);

        lesson.setPublished(true);
        lesson.setStatus(LessonStatus.PUBLISHED);
        lessonRepository.save(lesson);
    }

    // ==================================================================================
    // HELPERS: The "Diffing" Logic
    // ==================================================================================

    private void mergeSections(Lesson lesson, List<SectionDto.Upsert> incomingSections) {
        if (incomingSections == null) {
            lesson.getSections().clear();
            return;
        }

        List<Section> finalSections = new ArrayList<>();

        for (SectionDto.Upsert secDto : incomingSections) {
            Section section;
            if (secDto.id() != null) {
                // Try to find existing section
                section = lesson.getSections().stream()
                        .filter(s -> s.getId().equals(secDto.id()))
                        .findFirst()
                        .orElse(new Section());
            } else {
                section = new Section();
            }

            section.setLesson(lesson);
            section.setTitle(secDto.title());
            section.setPosition(secDto.position());

            mergeComponents(section, secDto.components());
            finalSections.add(section);
        }

        lesson.getSections().clear();
        lesson.getSections().addAll(finalSections);
    }

    private void mergeComponents(Section section, List<ComponentDto.Upsert> incomingComponents) {
        if (incomingComponents == null) {
            section.getComponents().clear();
            return;
        }

        List<Component> finalComponents = new ArrayList<>();

        for (ComponentDto.Upsert compDto : incomingComponents) {
            Component component;
            if (compDto.id() != null) {
                component = section.getComponents().stream()
                        .filter(c -> c.getId().equals(compDto.id()))
                        .findFirst()
                        .orElse(new Component());
            } else {
                component = new Component();
            }

            component.setSection(section);
            component.setPosition(compDto.position());
            component.setContent(compDto.content());
            component.syncType();

            finalComponents.add(component);
        }

        section.getComponents().clear();
        section.getComponents().addAll(finalComponents);
    }

    private void validateAuthor(Course course, UUID userId) {
        if (!course.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You are not the author of this course.");
        }
    }
}