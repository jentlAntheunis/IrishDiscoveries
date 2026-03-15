package irishdiscoveries.backend.domain;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "category")
    private java.util.List<Discovery> discoveries;
}
