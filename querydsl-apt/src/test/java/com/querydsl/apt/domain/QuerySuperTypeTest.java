package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.core.annotations.QuerySupertype;
import com.querydsl.apt.domain.QQuerySuperTypeTest_JdoEntity;
import com.querydsl.apt.domain.QQuerySuperTypeTest_Supertype;

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
        Assert.assertEquals(QQuerySuperTypeTest_Supertype.class,
                QQuerySuperTypeTest_JdoEntity.jdoEntity.references.any().getClass());
    }

}
