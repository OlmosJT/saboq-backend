package io.olmosjt.saboqbackend.domain.repository;

import io.olmosjt.saboqbackend.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    // Helpful for "My Courses" page
    // Note: This relies on the relation defined in the Course entity
     List<Course> findByAuthorId(UUID authorId);
}
