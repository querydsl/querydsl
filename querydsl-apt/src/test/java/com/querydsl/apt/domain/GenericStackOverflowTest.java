package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QuerySupertype;

public class GenericStackOverflowTest extends AbstractTest {

    public interface Identifiable<ID extends Comparable<ID> & Serializable> {
    }

    @QuerySupertype
    public abstract static class AbstractEntity<ID extends Comparable<ID> & Serializable> implements Identifiable<ID> {
    }

    @QueryEntity
    public static class TestEntity extends AbstractEntity<Long> {
    }

    @Test
    public void test() {
        assertNotNull(QGenericStackOverflowTest_AbstractEntity.abstractEntity);
    }
}
