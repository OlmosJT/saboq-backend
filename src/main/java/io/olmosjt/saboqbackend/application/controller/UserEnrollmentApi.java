package io.olmosjt.saboqbackend.application.controller;

import io.olmosjt.saboqbackend.domain.dto.EnrollmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/enrollments")
@Tag(name = "User Enrollments", description = "Manage learning progress and view enrolled courses")
@SecurityRequirement(name = "bearerAuth")
public interface UserEnrollmentApi {
    @GetMapping
    @Operation(summary = "Get My Enrollments", description = "List all courses the user is enrolled in, with progress.")
    ResponseEntity<List<EnrollmentDto.Response>> getMyEnrollments(
            @Parameter(hidden = true) UserDetails userDetails
    );

    @GetMapping("/{courseId}")
    @Operation(summary = "Get Enrollment Details", description = "Get specific enrollment info for a course.")
    ResponseEntity<EnrollmentDto.Response> getEnrollment(
            @PathVariable UUID courseId,
            @Parameter(hidden = true) UserDetails userDetails
    );

    @PatchMapping("/{courseId}/progress")
    @Operation(summary = "Update Progress", description = "Update the progress percentage for a specific course.")
    ResponseEntity<Void> updateProgress(
            @PathVariable UUID courseId,
            @RequestBody EnrollmentDto.UpdateProgressRequest request,
            @Parameter(hidden = true) UserDetails userDetails
    );
}
