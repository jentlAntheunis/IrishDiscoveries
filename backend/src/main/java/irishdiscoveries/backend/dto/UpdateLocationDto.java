package irishdiscoveries.backend.dto;

import irishdiscoveries.backend.domain.Location.BoundingBox;
import irishdiscoveries.backend.domain.Location.Coordinates;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateLocationDto extends BaseDto {
    private Coordinates coordinates;
    private BoundingBox boundingBox;
    private String placename;
    private String county;
    private String postcode;
    private String road;
}