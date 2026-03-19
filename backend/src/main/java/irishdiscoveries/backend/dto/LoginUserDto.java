package irishdiscoveries.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginUserDto extends BaseDto {
    private String username;
    private String password;
}
