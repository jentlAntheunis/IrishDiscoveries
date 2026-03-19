package irishdiscoveries.backend.domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Convert(converter = CoordinatesConverter.class)
    private Coordinates coordinates;
    @Convert(converter = BoundingBoxConverter.class)
    private BoundingBox boundingBox;
    private String placename;
    private String county;
    private String postcode;
    private String road;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private java.util.List<Discovery> discoveries;

    @Data
    public static class Coordinates {
        private Double lat;
        private Double lon;
    }

    @Data
    public static class BoundingBox {
        private Double north;
        private Double south;
        private Double east;
        private Double west;
    }
}

@Converter
class CoordinatesConverter implements AttributeConverter<Location.Coordinates, String> {
    @Override
    public String convertToDatabaseColumn(Location.Coordinates attribute) {
        if (attribute == null) {
            return null;
        }
        return formatNullable(attribute.getLat()) + "," + formatNullable(attribute.getLon());
    }

    @Override
    public Location.Coordinates convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        String[] values = dbData.split(",", -1);
        if (values.length != 2) {
            throw new IllegalArgumentException("Invalid coordinates format: " + dbData);
        }

        Location.Coordinates coordinates = new Location.Coordinates();
        coordinates.setLat(parseNullable(values[0]));
        coordinates.setLon(parseNullable(values[1]));
        return coordinates;
    }

    private String formatNullable(Double value) {
        return value == null ? "" : value.toString();
    }

    private Double parseNullable(String value) {
        String trimmed = value == null ? "" : value.trim();
        return trimmed.isEmpty() ? null : Double.valueOf(trimmed);
    }
}

@Converter
class BoundingBoxConverter implements AttributeConverter<Location.BoundingBox, String> {
    @Override
    public String convertToDatabaseColumn(Location.BoundingBox attribute) {
        if (attribute == null) {
            return null;
        }
        return formatNullable(attribute.getNorth()) + ","
                + formatNullable(attribute.getSouth()) + ","
                + formatNullable(attribute.getEast()) + ","
                + formatNullable(attribute.getWest());
    }

    @Override
    public Location.BoundingBox convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        String[] values = dbData.split(",", -1);
        if (values.length != 4) {
            throw new IllegalArgumentException("Invalid bounding box format: " + dbData);
        }

        Location.BoundingBox boundingBox = new Location.BoundingBox();
        boundingBox.setNorth(parseNullable(values[0]));
        boundingBox.setSouth(parseNullable(values[1]));
        boundingBox.setEast(parseNullable(values[2]));
        boundingBox.setWest(parseNullable(values[3]));
        return boundingBox;
    }

    private String formatNullable(Double value) {
        return value == null ? "" : value.toString();
    }

    private Double parseNullable(String value) {
        String trimmed = value == null ? "" : value.trim();
        return trimmed.isEmpty() ? null : Double.valueOf(trimmed);
    }
}
