package com.querydsl.example.jap.springboot.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.jap.springboot.model.Tweet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.querydsl.example.jap.springboot.model.QTweet.tweet;

@Repository
@Transactional
public class TweetRepository extends AbstractRepository<Tweet> {

    public Tweet save(Tweet tweet) {
        return persistOrMerge(tweet);
    }

    @Override
    public Tweet findById(Long id) {
        return find(Tweet.class, id);
    }

    public List<Tweet> findAll(Predicate expr) {
        return selectFrom(tweet).where(expr).fetch();
    }
}
