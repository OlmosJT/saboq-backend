package io.olmosjt.saboqbackend.application.controller;

import io.olmosjt.saboqbackend.domain.dto.CourseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/courses")
@Tag(name = "User Courses", description = "Manage created courses and enrollments")
@SecurityRequirement(name = "bearerAuth")
public interface UserCourseApi {

    @PostMapping
    @Operation(summary = "Create Course", description = "Create a new course as an author.")
    ResponseEntity<CourseDto.Summary> createCourse(
            @RequestBody CourseDto.CreateRequest request,
            @Parameter(hidden = true) UserDetails userDetails
    );

    @PutMapping("/{courseId}")
    @Operation(summary = "Update Course", description = "Update an existing course.")
    ResponseEntity<CourseDto.Summary> updateCourse(
            @PathVariable UUID courseId,
            @RequestBody CourseDto.UpdateRequest request,
            @Parameter(hidden = true) UserDetails userDetails
    );

    @DeleteMapping("/{courseId}")
    @Operation(summary = "Delete Course", description = "Delete a course (Author only).")
    ResponseEntity<Void> deleteCourse(
            @PathVariable UUID courseId,
            @Parameter(hidden = true) UserDetails userDetails
    );

    @GetMapping("/created")
    @Operation(summary = "Get Created Courses", description = "Get courses authored by the current user.")
    ResponseEntity<List<CourseDto.Summary>> getCreatedCourses(
            @Parameter(hidden = true) UserDetails userDetails
    );

    @PostMapping("/{courseId}/enroll")
    @Operation(summary = "Enroll in Course", description = "Enroll the current user in a course.")
    ResponseEntity<Void> enrollInCourse(
            @PathVariable UUID courseId,
            @Parameter(hidden = true) UserDetails userDetails
    );

    @GetMapping("/enrolled")
    @Operation(summary = "Get Enrolled Courses", description = "Get courses the user is currently learning.")
    ResponseEntity<List<CourseDto.Summary>> getEnrolledCourses(
            @Parameter(hidden = true) UserDetails userDetails
    );
}
