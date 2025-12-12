package io.olmosjt.saboqbackend.domain.service;

import io.olmosjt.saboqbackend.domain.dto.EnrollmentDto;
import io.olmosjt.saboqbackend.domain.entity.Enrollment;
import io.olmosjt.saboqbackend.domain.entity.composite.EnrollmentId;
import io.olmosjt.saboqbackend.domain.mapper.EnrollmentMapper;
import io.olmosjt.saboqbackend.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public List<EnrollmentDto.Response> getMyEnrollments(UUID userId) {
        return enrollmentRepository.findAllByUserId(userId).stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnrollmentDto.Response getEnrollment(UUID userId, UUID courseId) {
        Enrollment enrollment = enrollmentRepository.findById(new EnrollmentId(userId, courseId))
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        return EnrollmentMapper.toResponse(enrollment);
    }

    @Transactional
    public void updateProgress(UUID userId, UUID courseId, Float newProgress) {
        Enrollment enrollment = enrollmentRepository.findById(new EnrollmentId(userId, courseId))
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Basic validation (0 to 100)
        if (newProgress < 0 || newProgress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        enrollment.setProgress(newProgress);
        enrollment.setLastAccessed(LocalDateTime.now());
        enrollmentRepository.save(enrollment);
    }
}
