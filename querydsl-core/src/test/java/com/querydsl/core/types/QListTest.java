package com.querydsl.core.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.querydsl.core.types.dsl.Expressions;

public class QListTest {

    @Test
    public void newInstance() {
        QList qList = new QList(Expressions.stringPath("a"), Expressions.stringPath("b"));
        List<?> list = qList.newInstance("a", null);
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertNull(list.get(1));
    }

}
