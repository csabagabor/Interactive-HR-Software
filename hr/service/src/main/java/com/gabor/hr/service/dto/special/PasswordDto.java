package com.gabor.hr.service.dto.special;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {

    @NotBlank(message = "Password should not be blank")
    private String oldPassword;

    @Size(min = 8, message = "Password should have at least 8 characters")
    private String newPassword;

}
