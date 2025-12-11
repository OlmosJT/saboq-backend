package io.olmosjt.saboqbackend.domain.repository;

import io.olmosjt.saboqbackend.domain.entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComponentRepository extends JpaRepository<Component, UUID> {
    List<Component> findByType(String type);

    @Query(value = "SELECT * FROM components c WHERE c.type = 'TEXT' AND c.content ->> 'content' ILIKE %:searchTerm%", nativeQuery = true)
    List<Component> searchInTextComponents(String searchTerm);

}
