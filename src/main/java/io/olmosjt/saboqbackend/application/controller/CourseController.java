package io.olmosjt.saboqbackend.application.controller;

import io.olmosjt.saboqbackend.domain.dto.CourseDto;
import io.olmosjt.saboqbackend.domain.repository.UserRepository;
import io.olmosjt.saboqbackend.domain.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserRepository userRepository;

    /**
     * Helper to get UUID from the Security Context
     */
    private UUID getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    /**
     * 1. Create a new Course
     * POST /api/v1/courses
     */
    @PostMapping
    public ResponseEntity<CourseDto.CourseSummaryResponse> createCourse(
            @RequestBody CourseDto.CreateCourseRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = getUserId(userDetails);
        CourseDto.CourseSummaryResponse response = courseService.createCourse(userId, request);

        return ResponseEntity
                .created(URI.create("/api/v1/courses/" + response.id()))
                .body(response);
    }

    /**
     * 4. Update a Course
     * PUT /api/v1/courses/{courseId}
     */
    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDto.CourseSummaryResponse> updateCourse(
            @PathVariable UUID courseId,
            @RequestBody CourseDto.UpdateCourseRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = getUserId(userDetails);

        CourseDto.CourseSummaryResponse response =
                courseService.updateCourse(userId, courseId, request);

        return ResponseEntity.ok(response);
    }

    /**
     * 2. Get "My Courses" (Dashboard)
     * GET /api/v1/courses
     */
    @GetMapping
    public ResponseEntity<List<CourseDto.CourseSummaryResponse>> getMyCourses(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = getUserId(userDetails);
        return ResponseEntity.ok(courseService.getAllCoursesForUser(userId));
    }

    /**
     * 3. Get Course Details (with list of Lessons)
     * GET /api/v1/courses/{courseId}
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto.CourseDetailResponse> getCourseDetails(
            @PathVariable UUID courseId
    ) {
        return ResponseEntity.ok(courseService.getCourseDetails(courseId));
    }
}
