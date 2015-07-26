package com.querydsl.example.jpa.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.querydsl.example.jpa.repository.TweetRepository;
import com.querydsl.example.jpa.repository.UserRepository;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new JpaPersistModule("h2").properties(System.getProperties()));
        bind(JpaInitializer.class).asEagerSingleton();
        bind(TweetRepository.class).in(Scopes.SINGLETON);
        bind(UserRepository.class).in(Scopes.SINGLETON);
    }
}
