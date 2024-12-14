package org.taxi.repository;

import org.taxi.entity.User;
import org.taxi.dto.filters.UserFilter;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllUsersByFilter(UserFilter filter);
}
