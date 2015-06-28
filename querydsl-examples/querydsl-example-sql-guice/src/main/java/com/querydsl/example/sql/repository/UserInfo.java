package com.querydsl.example.sql.repository;

public class UserInfo {

    private final String username;

    private final long tweets;

    public UserInfo(String username, long tweets) {
        this.username = username;
        this.tweets = tweets;
    }

    public String getUsername() {
        return username;
    }

    public long getTweets() {
        return tweets;
    }

}
