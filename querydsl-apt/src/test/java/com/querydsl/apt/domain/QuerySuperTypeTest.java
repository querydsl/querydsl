package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;

import org.junit.Test;

import com.querydsl.core.annotations.QuerySupertype;

public class QuerySuperTypeTest {

    @QuerySupertype
    public static class Supertype {

    }

    @PersistenceCapable
    public static class JdoEntity {
        Set<Supertype> references;
    }

    @Test
    public void JdoEntity() {
        assertEquals(QQuerySuperTypeTest_Supertype.class,
                QQuerySuperTypeTest_JdoEntity.jdoEntity.references.any().getClass());
    }

}
