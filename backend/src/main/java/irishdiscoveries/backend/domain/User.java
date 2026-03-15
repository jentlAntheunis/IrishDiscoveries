package irishdiscoveries.backend.domain;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "user")
    private java.util.List<Discovery> discoveries;
}
