package com.querydsl.jpa.support;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;

import com.querydsl.jpa.domain.Cat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JPAPathBuilderValidatorTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("h2");
    }

    @After
    public void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    public void validate() {
        JPAPathBuilderValidator validator = new JPAPathBuilderValidator(entityManagerFactory.getMetamodel());
        assertEquals(String.class, validator.validate(Cat.class, "name", String.class));
        assertEquals(Cat.class, validator.validate(Cat.class, "kittens", Collection.class));
        assertEquals(Cat.class, validator.validate(Cat.class, "mate", Cat.class));
        assertNull(validator.validate(Cat.class, "xxx", String.class));
        assertNull(validator.validate(Object.class, "name", String.class));
    }
}
