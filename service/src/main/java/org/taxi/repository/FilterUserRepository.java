package org.taxi.repository;

import org.taxi.entity.User;
import org.taxi.filters.UserFilter;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllUsersByFilter(UserFilter filter);
}
