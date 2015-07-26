package com.querydsl.example.jpa.repository;

import static com.querydsl.example.jpa.model.QTweet.tweet;

import java.util.List;

import com.google.inject.persist.Transactional;
import com.mysema.query.types.Predicate;
import com.querydsl.example.jpa.model.Tweet;

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
        return from(tweet).where(expr).list(tweet);
    }
}
