package com.querydsl.example.jap.springboot.repository;

import com.querydsl.example.jap.springboot.BaseTest;
import com.querydsl.example.jap.springboot.model.User;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRepositoryTest extends BaseTest {
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
        assertTrue(repository.all().size() > 0);
    }
}
