package io.olmosjt.saboqbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avatars")
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String path;

    private String name;
}
