package org.taxi.dto;

import lombok.Value;
import org.taxi.entity.enums.Role;

@Value
public class UserReadDto {
    Long id;
    String firstname;
    String lastname;
    Role role;
    String email;
    String phone;
}
