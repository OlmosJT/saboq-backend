package io.olmosjt.saboqbackend.domain.service;

import io.olmosjt.saboqbackend.domain.dto.CourseDto;
import io.olmosjt.saboqbackend.domain.dto.LessonDtos;
import io.olmosjt.saboqbackend.domain.entity.Course;
import io.olmosjt.saboqbackend.domain.entity.User;
import io.olmosjt.saboqbackend.domain.enums.CourseStatus;
import io.olmosjt.saboqbackend.domain.mapper.LessonMapper;
import io.olmosjt.saboqbackend.domain.repository.CourseRepository;
import io.olmosjt.saboqbackend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;

    @Transactional
    public CourseDto.CourseSummaryResponse createCourse(UUID userId, CourseDto.CreateCourseRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = Course.builder()
                .title(request.title())
                .description(request.description())
                .thumbnailUrl(request.thumbnailUrl())
                .author(author)
                .isPublished(false) // Default draft
                .status(CourseStatus.ONGOING)
                .build();

        Course saved = courseRepository.save(course);
        return mapToSummary(saved);
    }

    @Transactional
    public CourseDto.CourseSummaryResponse updateCourse(UUID userId, UUID courseId, CourseDto.UpdateCourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this course");
        }

        if (request.title() != null) {
            course.setTitle(request.title());
        }

        if (request.description() != null) {
            course.setDescription(request.description());
        }

        if (request.thumbnailUrl() != null) {
            course.setThumbnailUrl(request.thumbnailUrl());
        }

        if (request.isPublished() != null) {
            course.setPublished(request.isPublished());
        }

        Course updated = courseRepository.save(course);
        return mapToSummary(updated);
    }


    @Transactional(readOnly = true)
    public List<CourseDto.CourseSummaryResponse> getAllCoursesForUser(UUID userId) {
        // Note: Ensure you added 'List<Course> findByAuthorId(UUID authorId);' to CourseRepository
        return courseRepository.findByAuthorId(userId).stream()
                .map(this::mapToSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseDto.CourseDetailResponse getCourseDetails(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Convert the list of Lesson entities to LessonSummaryResponse DTOs
        List<LessonDtos.LessonSummaryResponse> lessonSummaries = course.getLessons().stream()
                .map(lessonMapper::toSummary)
                .toList();

        return new CourseDto.CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailUrl(),
                course.getStatus(),
                course.isPublished(),
                course.getAuthor().getId(),
                lessonSummaries,
                course.getCreatedAt()
        );
    }

    private CourseDto.CourseSummaryResponse mapToSummary(Course c) {
        return new CourseDto.CourseSummaryResponse(
                c.getId(),
                c.getTitle(),
                c.getDescription(),
                c.getThumbnailUrl(),
                c.getStatus(),
                c.isPublished(),
                c.getAuthor().getId(),
                c.getCreatedAt()
        );
    }
}
