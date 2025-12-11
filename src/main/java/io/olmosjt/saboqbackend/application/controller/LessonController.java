package io.olmosjt.saboqbackend.application.controller;

import io.olmosjt.saboqbackend.domain.dto.LessonDtos;
import io.olmosjt.saboqbackend.domain.repository.UserRepository;
import io.olmosjt.saboqbackend.domain.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final UserRepository userRepository;

    private UUID getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }


    /**
     * 1. CREATE New Lesson (Draft)
     * POST /api/v1/courses/{courseId}/lessons
     * Payload: The full lesson structure (usually just title + empty sections initially)
     */
    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<UUID> createLesson(
            @PathVariable UUID courseId,
            @RequestBody LessonDtos.SaveLessonRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = getUserId(userDetails);

        // Pass userId to service to verify ownership
        UUID newLessonId = lessonService.saveLesson(userId, courseId, null, request);
        return ResponseEntity.created(URI.create("/api/v1/lessons/" + newLessonId)).body(newLessonId);
    }

    /**
     * 2. UPDATE Existing Lesson (Deep Save)
     * PUT /api/v1/lessons/{lessonId}
     * Payload: The full structure. Updates title, merges sections, updates components.
     */
    @PutMapping("/lessons/{lessonId}")
    public ResponseEntity<UUID> updateLesson(
            @PathVariable UUID lessonId,
            @RequestBody LessonDtos.SaveLessonRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = getUserId(userDetails);

        // Pass userId to service to verify ownership
        lessonService.saveLesson(userId, null, lessonId, request);
        return ResponseEntity.ok(lessonId);
    }

    /**
     * 3. GET Lesson (Load Editor / View)
     * GET /api/v1/lessons/{lessonId}
     * Returns: The deeply nested JSON for the frontend renderer.
     */
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonDtos.LessonDetailResponse> getLesson(@PathVariable UUID lessonId) {
        // Publicly accessible for now (or you can restrict to enrolled students later)
        return ResponseEntity.ok(lessonService.getLesson(lessonId));
    }

    /**
     * 4. PUBLISH Lesson
     * PATCH /api/v1/lessons/{lessonId}/publish
     */
    @PatchMapping("/lessons/{lessonId}/publish")
    public ResponseEntity<Void> publishLesson(
            @PathVariable UUID lessonId,
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = getUserId(userDetails);
        lessonService.publishLesson(userId, lessonId);
        return ResponseEntity.noContent().build();
    }

}
