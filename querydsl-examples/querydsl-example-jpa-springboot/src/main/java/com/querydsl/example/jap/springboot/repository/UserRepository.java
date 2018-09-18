package com.querydsl.example.jap.springboot.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.jap.springboot.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.querydsl.example.jap.springboot.model.QUser.user;

@Repository
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
