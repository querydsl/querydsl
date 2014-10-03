package com.mysema.query.jpa.support;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mysema.query.jpa.domain.Cat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertTrue(validator.validate(Cat.class, "name", String.class));
        assertFalse(validator.validate(Cat.class, "xxx", String.class));
        assertFalse(validator.validate(Object.class, "name", String.class));
    }
}
