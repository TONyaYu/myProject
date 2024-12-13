package org.taxi.dto;

import lombok.Value;
import org.taxi.entity.enums.Role;

@Value
public class UserReadDto {
    Long id;
    String firstName;
    String lastName;
    Role role;
    String emai;
    String phone;
}
