package com.querydsl.example.jpa.repository;

import com.google.inject.persist.Transactional;
import com.querydsl.core.types.Predicate;
import com.querydsl.example.jpa.model.User;

import java.util.List;

import static com.querydsl.example.jpa.model.QUser.user;

@Transactional
public class UserRepository extends AbstractRepository<User> {

    @Override
    public User findById(Long id) {
        return find(User.class, id);
    }

    public User save(User user) {
        return persistOrMerge(user);
    }

    public List<User> findAll(Predicate expr) {
        return selectFrom(user).where(expr).fetch();
    }

    public List<User> all() {
        return selectFrom(user).fetch();
    }
}
