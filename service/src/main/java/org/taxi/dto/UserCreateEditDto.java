package org.taxi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.taxi.entity.enums.Role;

@Value
@RequiredArgsConstructor
@FieldNameConstants
public class UserCreateEditDto {
    Long id;
    @NotBlank
    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 70)
    String firstname;
    @NotBlank(message = "Last name is required")
    String lastname;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;
    @NotBlank(message = "Phone is required")
    String phone;
    Role role;
    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be at least 3 characters")
    String password;
}
