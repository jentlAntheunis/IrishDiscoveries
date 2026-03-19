package irishdiscoveries.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateUserDto extends BaseDto {
    private String username;
    private String password;
}