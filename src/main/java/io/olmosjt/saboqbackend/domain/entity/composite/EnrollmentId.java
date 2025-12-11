package io.olmosjt.saboqbackend.domain.entity.composite;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class EnrollmentId implements Serializable {
    private UUID userId;
    private UUID courseId;
}
