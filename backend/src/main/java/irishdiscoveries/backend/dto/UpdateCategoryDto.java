package irishdiscoveries.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateCategoryDto extends BaseDto {
    private String name;
}