package org.taxi.dto;

import lombok.Value;
import org.taxi.entity.enums.UserRole;

@Value
public class UserCreateEditDto {
    String firstName;
    String lastName;
    UserRole userRole;
    String email;
    String phone;
}
