/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

/**
 * StringHandlingTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class StringHandlingTest extends AbstractQueryTest {

    private List<String> data1 = Arrays.asList("petER", "THomas", "joHAN");

    private List<String> data2 = Arrays.asList("PETer", "thOMAS", "JOhan");

    private List<String> data = Arrays.asList("abc", "aBC", "def");

    private final StringPath a = new StringPath("a");

    private final StringPath b = new StringPath("b");

    @Test
    public void equalsIgnoreCase() {
        Iterator<String> res = Arrays.asList("petER - PETer",
                "THomas - thOMAS", "joHAN - JOhan").iterator();
        for (Object[] arr : query()
                .from(a, data1)
                .from(b, data2)
                .where(a.equalsIgnoreCase(b)).list(a, b)) {
            assertEquals(res.next(), arr[0] + " - " + arr[1]);
        }
    }

    @Test
    public void startsWithIgnoreCase() {
        assertEquals(2, MiniApi.from(a, data).where(a.startsWith("AB", false)).count());
        assertEquals(2, MiniApi.from(a, data).where(a.startsWith("ab", false)).count());
    }

    @Test
    public void endsWithIgnoreCase() {
        assertEquals(2, MiniApi.from(a, data).where(a.endsWith("BC", false)).count());
        assertEquals(2, MiniApi.from(a, data).where(a.endsWith("bc", false)).count());
    }

}
