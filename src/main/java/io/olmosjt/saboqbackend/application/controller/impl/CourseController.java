package io.olmosjt.saboqbackend.application.controller.impl;

import io.olmosjt.saboqbackend.application.controller.PublicCourseApi;
import io.olmosjt.saboqbackend.application.controller.UserCourseApi;
import io.olmosjt.saboqbackend.domain.dto.CourseDto;
import io.olmosjt.saboqbackend.domain.service.AuthService;
import io.olmosjt.saboqbackend.domain.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CourseController implements PublicCourseApi, UserCourseApi {

    private final CourseService courseService;
    private final AuthService authService;


    @Override
    public ResponseEntity<Page<CourseDto.Summary>> getPublicCatalog(Pageable pageable) {
        return ResponseEntity.ok(courseService.getAllPublishedCourses(pageable));
    }

    @Override
    public ResponseEntity<CourseDto.Detail> getCourseDetails(UUID courseId) {
        return ResponseEntity.ok(courseService.getCourseDetails(courseId));
    }

    @Override
    public ResponseEntity<CourseDto.Summary> createCourse(
            CourseDto.CreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);
        CourseDto.Summary response = courseService.createCourse(userId, request);
        return ResponseEntity
                .created(URI.create("/api/v1/courses/" + response.id()))
                .body(response);
    }

    @Override
    public ResponseEntity<CourseDto.Summary> updateCourse(
            UUID courseId,
            CourseDto.UpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);
        return ResponseEntity.ok(courseService.updateCourse(userId, courseId, request));
    }

    @Override
    public ResponseEntity<Void> deleteCourse(
            UUID courseId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);
        courseService.deleteCourse(userId, courseId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<CourseDto.Summary>> getCreatedCourses(
           @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);
        return ResponseEntity.ok(courseService.getCreatedCourses(userId));
    }

    @Override
    public ResponseEntity<Void> enrollInCourse(UUID courseId, @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = authService.getUserId(userDetails);
        courseService.enrollUser(userId, courseId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CourseDto.Summary>> getEnrolledCourses(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = authService.getUserId(userDetails);
        return ResponseEntity.ok(courseService.getEnrolledCourses(userId));
    }
}
