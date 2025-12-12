package io.olmosjt.saboqbackend.application.controller;

import io.olmosjt.saboqbackend.domain.dto.LessonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1")
@Tag(name = "User Lessons", description = "Manage lesson creation and updates")
@SecurityRequirement(name = "bearerAuth")
public interface UserLessonApi {

    @PostMapping("/courses/{courseId}/lessons")
    @Operation(summary = "Create Lesson", description = "Create a new draft lesson within a course.")
    ResponseEntity<UUID> createLesson(
            @PathVariable UUID courseId,
            @RequestBody LessonDto.UpsertRequest request,
            @Parameter(hidden = true) UserDetails userDetails
    );

    @PutMapping("/lessons/{lessonId}")
    @Operation(summary = "Update Lesson", description = "Save changes to a lesson (Deep Save).")
    ResponseEntity<UUID> updateLesson(
            @PathVariable UUID lessonId,
            @RequestBody LessonDto.UpsertRequest request,
            @Parameter(hidden = true) UserDetails userDetails
    );

    @PatchMapping("/lessons/{lessonId}/publish")
    @Operation(summary = "Publish Lesson", description = "Mark a lesson as published.")
    ResponseEntity<Void> publishLesson(
            @PathVariable UUID lessonId,
            @Parameter(hidden = true) UserDetails userDetails
    );

}
