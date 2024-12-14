package org.taxi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.taxi.dto.UserCreateEditDto;
import org.taxi.entity.User;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);

        return user;
    }

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setFirstName(object.getFirstname());
        user.setLastName(object.getLastname());
        user.setEmail(object.getEmail());
        user.setRole(object.getRole());
        user.setPhone(object.getPhone());
    }
}
