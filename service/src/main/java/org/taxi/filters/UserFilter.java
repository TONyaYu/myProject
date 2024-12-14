package org.taxi.filters;

import lombok.Value;

@Value
public class UserFilter {
    String firstname;
    String lastname;
    String email;
    String phone;
}
