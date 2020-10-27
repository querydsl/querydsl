package com.querydsl.example.sql.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.example.sql.guice.Transactional;
import com.querydsl.example.sql.model.User;

import java.util.List;

import static com.querydsl.example.sql.model.QTweet.tweet;
import static com.querydsl.example.sql.model.QUser.user;

public class UserRepository extends AbstractRepository {
    @Transactional
    public User findById(Long id) {
        return selectFrom(user).where(user.id.eq(id)).fetchOne().orElse(null);
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
        return select(Projections.constructor(UserInfo.class, user.username, tweet.id.count())).from(user)
                .leftJoin(tweet).on(user.id.eq(tweet.posterId))
                .groupBy(user.username)
                .fetch();
    }

    @Transactional
    public List<User> findAll(Predicate expr) {
        return selectFrom(user).where(expr).fetch();
    }

    @Transactional
    public List<User> all() {
        return selectFrom(user).fetch();
    }

}
