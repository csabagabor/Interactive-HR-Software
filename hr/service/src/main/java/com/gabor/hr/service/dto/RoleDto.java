package com.gabor.hr.service.dto;


import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto extends BaseDto {
    @NotBlank(message = "Name must not be empty")
    @Size(max = Constants.ROLE_NAME_LENGTH_MAX, message = "Role Name too long")
    private String name;
}
