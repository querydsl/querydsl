package com.querydsl.example.sql.repository;

import static com.querydsl.example.sql.model.QTweet.tweet;
import static com.querydsl.example.sql.model.QUser.user;

import java.util.List;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;
import com.querydsl.example.sql.guice.Transactional;
import com.querydsl.example.sql.model.User;

public class UserRepository extends AbstractRepository {
    @Transactional
    public User findById(Long id) {
        return from(user).where(user.id.eq(id)).singleResult(user);
    }

    @Transactional
    public Long save(User entity) {
        if (entity.getId() != null) {
            update(user).populate(entity).execute();
            return entity.getId();
        }
        return insert(user).populate(entity)
                .executeWithKey(user.id);
    }

    @Transactional
    public List<UserInfo> allWithTweetCount() {
        return from(user)
                .leftJoin(tweet).on(user.id.eq(tweet.posterId))
                .groupBy(user.username)
                .list(Projections.constructor(UserInfo.class, user.username, tweet.id.count()));
    }

    @Transactional
    public List<User> findAll(Predicate expr) {
        return from(user).where(expr).list(user);
    }

    @Transactional
    public List<User> all() {
        return from(user).list(user);
    }

}
