package io.olmosjt.saboqbackend.domain.repository;

import io.olmosjt.saboqbackend.domain.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section, UUID> {}
