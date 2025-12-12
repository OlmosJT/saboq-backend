package io.olmosjt.saboqbackend.domain.service;

import io.olmosjt.saboqbackend.domain.dto.CourseDto;
import io.olmosjt.saboqbackend.domain.dto.LessonDto;
import io.olmosjt.saboqbackend.domain.entity.Course;
import io.olmosjt.saboqbackend.domain.entity.Enrollment;
import io.olmosjt.saboqbackend.domain.entity.User;
import io.olmosjt.saboqbackend.domain.entity.composite.EnrollmentId;
import io.olmosjt.saboqbackend.domain.mapper.CourseMapper;
import io.olmosjt.saboqbackend.domain.mapper.LessonMapper;
import io.olmosjt.saboqbackend.domain.repository.CourseRepository;
import io.olmosjt.saboqbackend.domain.repository.EnrollmentRepository;
import io.olmosjt.saboqbackend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonMapper lessonMapper;

    // --- CREATOR ACTIONS ---

    @Transactional
    public CourseDto.Summary createCourse(UUID userId, CourseDto.CreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = Course.builder()
                .title(request.title())
                .description(request.description())
                .thumbnailUrl(request.thumbnailUrl())
                .author(author)
                .build();

        Course saved = courseRepository.save(course);
        return CourseMapper.toSummary(saved);
    }

    @Transactional
    public CourseDto.Summary updateCourse(UUID userId, UUID courseId, CourseDto.UpdateRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        validateAuthor(course, userId);

        if (request.title() != null) course.setTitle(request.title());
        if (request.description() != null) course.setDescription(request.description());
        if (request.thumbnailUrl() != null) course.setThumbnailUrl(request.thumbnailUrl());
        if (request.isPublished() != null) course.setPublished(request.isPublished());

        Course updated = courseRepository.save(course);
        return CourseMapper.toSummary(updated);
    }

    @Transactional
    public void deleteCourse(UUID userId, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        validateAuthor(course, userId);
        courseRepository.delete(course);
    }

    @Transactional(readOnly = true)
    public List<CourseDto.Summary> getCreatedCourses(UUID userId) {
        return courseRepository.findByAuthor_Id(userId).stream()
                .map(CourseMapper::toSummary)
                .toList();
    }

    // --- PUBLIC / CATALOG ACTIONS ---

    @Transactional(readOnly = true)
    public Page<CourseDto.Summary> getAllPublishedCourses(Pageable pageable) {
        return courseRepository.findByIsPublishedTrue(pageable)
                .map(CourseMapper::toSummary);
    }

    @Transactional(readOnly = true)
    public CourseDto.Detail getCourseDetails(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<LessonDto.LessonSummaryResponse> lessonSummaries = course.getLessons().stream()
                .map(lessonMapper::toSummary)
                .toList();

        // Use the Mapper to ensure AuthorInfo is correctly populated
        return CourseMapper.toDetail(course, lessonSummaries);
    }

    // --- LEARNER ACTIONS ---

    @Transactional
    public void enrollUser(UUID userId, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Allow authors to enroll in their own course (preview),
        // otherwise restrict to published courses.
        if (!course.isPublished() && !course.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("Cannot enroll in an unpublished course");
        }

        EnrollmentId id = new EnrollmentId(userId, courseId);
        if (enrollmentRepository.existsById(id)) {
            return;
        }

        User user = userRepository.getReferenceById(userId);

        Enrollment enrollment = Enrollment.builder()
                .id(id)
                .user(user)
                .course(course)
                .progress(0.0f)
                .lastAccessed(LocalDateTime.now())
                .build();

        enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public List<CourseDto.Summary> getEnrolledCourses(UUID userId) {
        return enrollmentRepository.findAllByUserId(userId).stream()
                .map(enrollment -> CourseMapper.toSummary(enrollment.getCourse()))
                .toList();
    }

    // --- HELPERS ---

    private void validateAuthor(Course course, UUID userId) {
        if (!course.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to modify this course");
        }
    }
}