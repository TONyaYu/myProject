package org.taxi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;
import org.taxi.entity.enums.Role;

@Value
@FieldNameConstants
public class UserCreateEditDto {
    @NotBlank
    @Size(min = 3, max = 70)
    String firstname;
    @NotBlank
    String lastname;
    @Email
    String email;
    String phone;
    Role role;
    @NotBlank
    String password;
}
