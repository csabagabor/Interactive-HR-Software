package com.gabor.hr.service.dto.special;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserViewDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String roleName;

}
