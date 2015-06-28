package com.querydsl.example.jpa.repository;

import static com.querydsl.example.jpa.model.QUser.user;

import java.util.List;

import com.google.inject.persist.Transactional;
import com.mysema.query.types.Predicate;
import com.querydsl.example.jpa.model.User;

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
        return from(user).where(expr).list(user);
    }
    
    public List<User> all() {
        return from(user).list(user);
    }
}
