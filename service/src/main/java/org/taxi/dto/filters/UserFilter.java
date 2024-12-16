package org.taxi.dto.filters;

import lombok.Value;
import org.taxi.entity.enums.Role;

@Value
public class UserFilter {
    String firstname;
    String lastname;
    String email;
    String phone;
    Role role;
}
