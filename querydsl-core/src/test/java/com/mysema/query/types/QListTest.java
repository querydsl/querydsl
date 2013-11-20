package com.mysema.query.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class QListTest {

    @Test
    public void NewInstance() {
        QList qList = new QList(new StringPath("a"), new StringPath("b"));
        List<?> list = qList.newInstance("a", null);
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertNull(list.get(1));
    }

}
