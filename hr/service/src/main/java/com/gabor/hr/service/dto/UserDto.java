package com.gabor.hr.service.dto;

import com.gabor.model.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto {

    @NotBlank(message = "First Name must not be empty")
    @Size(max = Constants.USER_FIRST_NAME_LENGTH_MAX, message = "First Name too long")
    private String firstName;

    @NotBlank(message = "Last Name must not be empty")
    @Size(max = Constants.USER_LAST_NAME_LENGTH_MAX, message = "Last Name too long")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @Size(max = Constants.USER_EMAIL_LENGTH_MAX, message = "Email too long")
    @NotBlank(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "Role must not be empty")
    @Pattern(regexp = "ROLE_ADMIN|ROLE_USER|ROLE_MODERATOR", message = "Incorrect Role")
    private String roleName;
}
