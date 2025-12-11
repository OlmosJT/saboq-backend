package io.olmosjt.saboqbackend.domain.entity;

import io.olmosjt.saboqbackend.domain.ComponentContent;
import io.olmosjt.saboqbackend.domain.enums.ComponentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "components")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @ToString.Exclude
    private Section section;

    // Discriminator for SQL queries (Redundant but useful for indexing)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComponentType type;

    // THE HYBRID PART: Maps JSONB to your Java Records
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private ComponentContent content;

    @Column(nullable = false)
    private Integer position;

    // Auto-sync the 'type' field before saving based on the content object
    @PrePersist
    @PreUpdate
    public void syncType() {
        if (content != null) {
            this.type = content.type();
        }
    }
}
