package irishdiscoveries.backend.domain;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Double longitude;
    private Double latitude;
    private String mapsUrl;
    private String city;
    private String county;
    private String postcode;
    private String road;

    @OneToMany(mappedBy = "location")
    private java.util.List<Discovery> discoveries;
}
