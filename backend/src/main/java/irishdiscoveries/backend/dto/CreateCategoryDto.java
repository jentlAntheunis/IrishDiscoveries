package irishdiscoveries.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateCategoryDto extends BaseDto {
    private String name;
}