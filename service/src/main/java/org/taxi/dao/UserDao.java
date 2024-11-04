package org.taxi.dao;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Cleanup;
import org.hibernate.SessionFactory;
import org.taxi.entity.QUser;
import org.taxi.entity.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.taxi.entity.QUser.user;

public class UserDao implements DAO<Long, User>{

    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User entity) {
        @Cleanup var session = sessionFactory.openSession();
        session.persist(entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        @Cleanup var session = sessionFactory.openSession();
        session.remove(id);
        session.flush();
    }

    @Override
    public void update(User entity) {
        @Cleanup var session = sessionFactory.openSession();
        session.merge(entity);

    }

    @Override
    public Optional<User> findById(Long id) {
        @Cleanup var session = sessionFactory.openSession();

        return Optional.ofNullable(session.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        @Cleanup var session = sessionFactory.openSession();
        return new JPAQuery<User>((EntityManager) session)
                .select(user)
                .from(user)
                .fetch();
    }
}
