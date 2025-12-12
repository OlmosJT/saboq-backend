package io.olmosjt.saboqbackend.domain.mapper;

import io.olmosjt.saboqbackend.domain.dto.EnrollmentDto;
import io.olmosjt.saboqbackend.domain.entity.Enrollment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnrollmentMapper {
    public static EnrollmentDto.Response toResponse(Enrollment enrollment) {
        return new EnrollmentDto.Response(
                CourseMapper.toSummary(enrollment.getCourse()),
                enrollment.getProgress(),
                enrollment.getLastAccessed()
        );
    }
}
