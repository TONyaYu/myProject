package org.taxi.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.taxi.entity.enums.Role;

@Value
@RequiredArgsConstructor
public class UserReadDto {
    Long id;
    String firstname;
    String lastname;
    Role role;
    String email;
    String phone;
    String password;
}
