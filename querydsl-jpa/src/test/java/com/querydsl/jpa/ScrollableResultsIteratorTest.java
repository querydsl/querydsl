package com.querydsl.jpa;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.hibernate.ScrollableResults;
import org.junit.Test;

public class ScrollableResultsIteratorTest {

    @Test
    public void extractValue_hibernate5StyleObjectArray_singleColumn() {
        assertEquals("entity", ScrollableResultsIterator.extractValue(new Object[]{"entity"}, false));
    }

    @Test
    public void extractValue_hibernate5StyleObjectArray_asArray() {
        Object[] row = new Object[]{"a", "b"};
        assertArrayEquals(row, (Object[]) ScrollableResultsIterator.extractValue(row, true));
    }

    @Test
    public void extractValue_hibernate6StyleSingleEntity() {
        Object entity = new Object();
        assertEquals(entity, ScrollableResultsIterator.extractValue(entity, false));
    }

    @Test
    public void extractValue_hibernate6StyleSingleEntity_asArray() {
        Object entity = new Object();
        assertArrayEquals(new Object[]{entity},
                (Object[]) ScrollableResultsIterator.extractValue(entity, true));
    }

    @Test
    public void extractValue_emptyObjectArray() {
        assertNull(ScrollableResultsIterator.extractValue(new Object[0], false));
    }

    @Test
    public void next_usesGetWithoutIndex_forSingleValue() {
        ScrollableResults results = createMock(ScrollableResults.class);
        expect(results.next()).andReturn(true);
        expect(results.get()).andReturn(new Object[]{"cat"});
        expect(results.next()).andReturn(false);
        replay(results);

        ScrollableResultsIterator<String> iterator = new ScrollableResultsIterator<String>(results);
        assertTrue(iterator.hasNext());
        assertEquals("cat", iterator.next());
        assertFalse(iterator.hasNext());
        verify(results);
    }

    @Test
    public void next_asArray_returnsFullRow() {
        ScrollableResults results = createMock(ScrollableResults.class);
        Object[] row = new Object[]{"name", "birthdate"};
        expect(results.next()).andReturn(true);
        expect(results.get()).andReturn(row);
        replay(results);

        ScrollableResultsIterator<Object[]> iterator =
                new ScrollableResultsIterator<Object[]>(results, true);
        assertArrayEquals(row, iterator.next());
        verify(results);
    }

    @Test(expected = NoSuchElementException.class)
    public void next_whenEmpty_throws() {
        ScrollableResults results = createMock(ScrollableResults.class);
        expect(results.next()).andReturn(false);
        replay(results);

        new ScrollableResultsIterator<Object>(results).next();
    }

}
