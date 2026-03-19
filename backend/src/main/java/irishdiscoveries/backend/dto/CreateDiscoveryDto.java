package irishdiscoveries.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateDiscoveryDto extends BaseDto {
    private String name;
    private String description;
    private String priceCategory;
    private LocalDateTime discoveredOn;
    private UUID categoryId;
    private UUID locationId;
    private UUID userId;
}
