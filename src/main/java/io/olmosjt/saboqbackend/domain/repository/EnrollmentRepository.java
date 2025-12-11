package io.olmosjt.saboqbackend.domain.repository;

import io.olmosjt.saboqbackend.domain.entity.Enrollment;
import io.olmosjt.saboqbackend.domain.entity.composite.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {}
