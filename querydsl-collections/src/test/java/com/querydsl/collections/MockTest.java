package com.querydsl.collections;

import static org.junit.Assert.*;

import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.querydsl.core.types.path.SimplePath;

public class MockTest {
    
    @Test
    public void test() {
        List<MockTest> tests = Lists.newArrayList(new MockTest(), new MockTest(), new MockTest());
        SimplePath<MockTest> path = new SimplePath<MockTest>(MockTest.class, "obj");
        MockTest mock = EasyMock.createMock(MockTest.class);
        assertTrue(CollQueryFactory.from(path, tests).where(path.eq(mock)).list(path).isEmpty());
    }

}
