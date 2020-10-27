package com.querydsl.collections;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;

public class MockTest {

    @Test
    public void test() {
        List<MockTest> tests = Arrays.asList(new MockTest(), new MockTest(), new MockTest());
        SimplePath<MockTest> path = Expressions.path(MockTest.class, "obj");
        MockTest mock = EasyMock.createMock(MockTest.class);
        assertTrue(CollQueryFactory.from(path, tests).where(path.eq(mock)).fetch().isEmpty());
    }

}
