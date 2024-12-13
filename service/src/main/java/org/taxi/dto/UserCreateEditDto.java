package org.taxi.dto;

import lombok.Value;
import org.taxi.entity.enums.Role;

@Value
public class UserCreateEditDto {
    String firstName;
    String lastName;
    String email;
    String phone;
    Role role;
}
