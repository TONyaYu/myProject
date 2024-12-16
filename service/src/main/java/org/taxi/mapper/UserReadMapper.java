package org.taxi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.taxi.dto.UserReadDto;
import org.taxi.entity.User;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(
                object.getId(),
                object.getFirstName(),
                object.getLastName(),
                object.getRole(),
                object.getEmail(),
                object.getPhone()
        );
    }
}
