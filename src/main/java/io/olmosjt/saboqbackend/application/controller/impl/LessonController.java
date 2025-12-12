package io.olmosjt.saboqbackend.application.controller.impl;


import io.olmosjt.saboqbackend.application.controller.PublicLessonApi;
import io.olmosjt.saboqbackend.application.controller.UserLessonApi;
import io.olmosjt.saboqbackend.domain.dto.LessonDto;
import io.olmosjt.saboqbackend.domain.service.AuthService;
import io.olmosjt.saboqbackend.domain.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LessonController implements PublicLessonApi, UserLessonApi {

    private final LessonService lessonService;
    private final AuthService authService;

    // ==========================================
    // PUBLIC API
    // ==========================================

    @Override
    public ResponseEntity<LessonDto.Detail> getLesson(UUID lessonId) {
        // TODO: Future: Check if user owns the course or if it's free/previewable
        return ResponseEntity.ok(lessonService.getLesson(lessonId));
    }

    // ==========================================
    // USER / AUTHOR API
    // ==========================================

    @Override
    public ResponseEntity<UUID> createLesson(
            UUID courseId,
            LessonDto.UpsertRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);

        UUID newLessonId = lessonService.createLesson(userId, courseId, request);

        return ResponseEntity
                .created(URI.create("/api/v1/lessons/" + newLessonId))
                .body(newLessonId);
    }

    @Override
    public ResponseEntity<UUID> updateLesson(
            UUID lessonId,
            LessonDto.UpsertRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);

        // Uses the specific 'update' method in service
        return ResponseEntity.ok(lessonService.updateLesson(userId, lessonId, request));
    }

    @Override
    public ResponseEntity<Void> publishLesson(
            UUID lessonId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);
        lessonService.publishLesson(userId, lessonId);
        return ResponseEntity.noContent().build();
    }
}