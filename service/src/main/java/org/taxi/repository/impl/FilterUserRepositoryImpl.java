package org.taxi.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.taxi.entity.User;
import org.taxi.dto.filters.UserFilter;
import org.taxi.repository.FilterUserRepository;
import org.taxi.util.QueryDslPredicate;

import java.util.List;

import static org.taxi.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllUsersByFilter(UserFilter filter) {

        Predicate predicate = QueryDslPredicate.builder()
                .add(filter.getFirstname(), user.firstName::containsIgnoreCase)
                .add(filter.getLastname(), user.lastName::containsIgnoreCase)
                .add(filter.getEmail(), user.email::containsIgnoreCase)
                .add(filter.getPhone(), user.phone::contains)
                .buildOr();
        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .fetch();
    }
}
