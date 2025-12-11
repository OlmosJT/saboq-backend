package io.olmosjt.saboqbackend.domain.service;

import io.olmosjt.saboqbackend.domain.dto.ComponentDtos;
import io.olmosjt.saboqbackend.domain.dto.LessonDtos;
import io.olmosjt.saboqbackend.domain.dto.SectionDtos;
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
    private final LessonMapper lessonMapper;

    /**
     * Handles both CREATING (New Lesson) and UPDATING (Save Draft).
     *
     * @param courseId Only needed if creating a new lesson (lessonId is null).
     * @param lessonId If provided, we update this existing lesson.
     * @param request  The full deep structure of the lesson.
     * @return The ID of the saved lesson.
     */
    @Transactional
    public UUID saveLesson(UUID userId, UUID courseId, UUID lessonId, LessonDtos.SaveLessonRequest request) {
        Lesson lesson;

        if (lessonId == null) {
            // --- CREATE MODE ---
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // SECURITY CHECK: Is the actor the author?
            validateAuthor(course, userId);

            lesson = Lesson.builder()
                    .course(course)
                    .title(request.title())
                    .position(request.position() != null ? request.position() : 0)
                    .isPublished(request.isPublished())
                    .status(request.isPublished() ? LessonStatus.PUBLISHED : LessonStatus.DRAFT)
                    .build();
        } else {
            // --- UPDATE MODE ---
            lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));

            // SECURITY CHECK
            validateAuthor(lesson.getCourse(), userId);

            lesson.setTitle(request.title());
            if (request.position() != null) lesson.setPosition(request.position());
            lesson.setPublished(request.isPublished());
            lesson.setStatus(request.isPublished() ? LessonStatus.PUBLISHED : LessonStatus.DRAFT);
        }

        mergeSections(lesson, request.sections());
        return lessonRepository.save(lesson).getId();
    }

    public LessonDtos.LessonDetailResponse getLesson(UUID lessonId) {
        return null;
    }

    @Transactional
    public void publishLesson(UUID userId, UUID lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // SECURITY CHECK
        validateAuthor(lesson.getCourse(), userId);

        lesson.setPublished(true);
        lesson.setStatus(LessonStatus.PUBLISHED);
        lessonRepository.save(lesson);
    }

    // ==================================================================================
    // HELPERS: The "Diffing" Logic
    // This logic ensures we update existing rows instead of deleting/re-inserting everything.
    // ==================================================================================

    private void mergeSections(Lesson lesson, List<SectionDtos.SectionDto> incomingSections) {
        if (incomingSections == null) {
            lesson.getSections().clear();
            return;
        }

        List<Section> finalSections = new ArrayList<>();

        for (SectionDtos.SectionDto secDto : incomingSections) {
            Section section;
            if (secDto.id() != null) {
                // Try to find existing section in the database list
                section = lesson.getSections().stream()
                        .filter(s -> s.getId().equals(secDto.id()))
                        .findFirst()
                        .orElse(new Section()); // Fallback: Treat as new if ID not found
            } else {
                section = new Section();
            }

            // Update simple fields
            section.setLesson(lesson); // Parent reference
            section.setTitle(secDto.title());
            section.setPosition(secDto.position());

            // Recursively merge components
            mergeComponents(section, secDto.components());

            finalSections.add(section);
        }

        // This is the magic part:
        // By clearing and adding, Hibernate's orphanRemoval=true will DELETE any sections
        // that were in the DB but are NOT in 'finalSections'.
        lesson.getSections().clear();
        lesson.getSections().addAll(finalSections);
    }

    private void mergeComponents(Section section, List<ComponentDtos.ComponentDto> incomingComponents) {
        if (incomingComponents == null) {
            section.getComponents().clear();
            return;
        }

        List<Component> finalComponents = new ArrayList<>();

        for (ComponentDtos.ComponentDto compDto : incomingComponents) {
            Component component;
            if (compDto.id() != null) {
                // Try to find existing component
                component = section.getComponents().stream()
                        .filter(c -> c.getId().equals(compDto.id()))
                        .findFirst()
                        .orElse(new Component());
            } else {
                component = new Component();
            }

            // Update fields
            component.setSection(section); // Parent reference
            component.setPosition(compDto.position());
            component.setContent(compDto.content()); // The JSONB Payload

            // Important: Helper method in Component entity syncs the discriminator 'type'
            component.syncType();

            finalComponents.add(component);
        }

        // Hibernate orphanRemoval handles deletions
        section.getComponents().clear();
        section.getComponents().addAll(finalComponents);
    }

    private void validateAuthor(Course course, UUID userId) {
        if (!course.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You are not the author of this course.");
        }
    }


}
