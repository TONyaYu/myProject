package org.taxi.dao;

import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.taxi.entity.BaseEntity;
import com.querydsl.jpa.impl.JPAQuery;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private final Class<E> clazz;
    private final SessionFactory sessionFactory;
    private final EntityPathBase<E> entityPath;

    @Override
    public E save(E entity) {
        var session = sessionFactory.getCurrentSession();
        session.persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        var session = sessionFactory.getCurrentSession();
        session.remove(id);
        session.flush();
    }

    @Override
    public void update(E entity) {
        var session = sessionFactory.getCurrentSession();
        session.merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        var session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        var session = sessionFactory.getCurrentSession();
        return new JPAQuery<E>(session).select(entityPath).from(entityPath).fetch();
    }
}
