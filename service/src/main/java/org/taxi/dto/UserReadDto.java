package org.taxi.dto;

import org.taxi.entity.enums.UserRole;

public record UserReadDto(Long id,
                          String firstName,
                          String lastName,
                          UserRole userRole,
                          String email,
                          String phone) {

}
