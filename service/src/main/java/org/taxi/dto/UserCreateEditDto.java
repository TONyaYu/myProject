package org.taxi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.taxi.entity.enums.Role;

@Value
@FieldNameConstants
public class UserCreateEditDto {
    @Size(min = 3, max = 70)
    String firstname;
    String lastname;
    @Email
    String email;
    String phone;
    Role role;
}
