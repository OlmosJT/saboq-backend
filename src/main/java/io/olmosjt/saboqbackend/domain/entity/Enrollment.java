package io.olmosjt.saboqbackend.domain.entity;

import io.olmosjt.saboqbackend.domain.entity.composite.EnrollmentId;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Maps the 'userId' field in Embeddable to this relation
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId") // Maps the 'courseId' field in Embeddable to this relation
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    @Builder.Default
    private Float progress = 0.0f;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;
}
