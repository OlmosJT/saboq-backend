package io.olmosjt.saboqbackend.application.controller;

import io.olmosjt.saboqbackend.domain.dto.LessonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api/v1/lessons")
@Tag(name = "Public Lessons", description = "View lesson content")
public interface PublicLessonApi {

    @GetMapping("/{lessonId}")
    @Operation(summary = "Get Lesson Content", description = "Retrieve the full lesson structure for the player/editor.")
    ResponseEntity<LessonDto.Detail> getLesson(@PathVariable UUID lessonId);

}
