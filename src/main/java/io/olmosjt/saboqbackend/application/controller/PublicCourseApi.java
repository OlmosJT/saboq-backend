package io.olmosjt.saboqbackend.application.controller;

import io.olmosjt.saboqbackend.domain.dto.CourseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api/v1/courses")
@Tag(name = "Public Courses", description = "Public catalog and course details")
public interface PublicCourseApi {

    @GetMapping
    @Operation(summary = "Get Public Catalog", description = "Retrieve paginated published courses.")
    ResponseEntity<Page<CourseDto.Summary>> getPublicCatalog(
            @ParameterObject Pageable pageable
    );

    @GetMapping("/{courseId}")
    @Operation(summary = "Get Course Details", description = "Retrieve details for a specific course.")
    ResponseEntity<CourseDto.Detail> getCourseDetails(@PathVariable UUID courseId);

}
