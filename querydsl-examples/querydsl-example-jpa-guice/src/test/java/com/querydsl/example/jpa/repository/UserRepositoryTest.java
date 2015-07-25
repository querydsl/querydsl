package com.querydsl.example.jpa.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.Test;

import com.querydsl.example.jpa.model.User;
import com.querydsl.example.jpa.repository.UserRepository;

public class UserRepositoryTest extends AbstractPersistenceTest {
    @Inject
    private UserRepository repository;

    @Test
    public void save_and_get_by_id() {
        String username = "jackie";
        User user = new User(username);
        repository.save(user);
        assertEquals(username, repository.findById(user.getId()).getUsername());
    }

    @Test
    public void get_all() {
        repository.save(new User("jimmy"));
        assertTrue(repository.all().size() == 1);
    }
}
