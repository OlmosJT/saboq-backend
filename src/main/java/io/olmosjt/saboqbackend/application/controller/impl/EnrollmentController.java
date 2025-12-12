package io.olmosjt.saboqbackend.application.controller.impl;

import io.olmosjt.saboqbackend.application.controller.UserEnrollmentApi;
import io.olmosjt.saboqbackend.domain.dto.EnrollmentDto;
import io.olmosjt.saboqbackend.domain.service.AuthService;
import io.olmosjt.saboqbackend.domain.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EnrollmentController implements UserEnrollmentApi {

    private final EnrollmentService enrollmentService;
    private final AuthService authService;

    @Override
    public ResponseEntity<List<EnrollmentDto.Response>> getMyEnrollments(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(userId));
    }

    @Override
    public ResponseEntity<EnrollmentDto.Response> getEnrollment(
            UUID courseId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);
        return ResponseEntity.ok(enrollmentService.getEnrollment(userId, courseId));
    }

    @Override
    public ResponseEntity<Void> updateProgress(
            UUID courseId,
            EnrollmentDto.UpdateProgressRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = authService.getUserId(userDetails);
        enrollmentService.updateProgress(userId, courseId, request.progress());
        return ResponseEntity.noContent().build();
    }
}
