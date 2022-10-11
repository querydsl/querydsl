package com.querydsl.jpa;

import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.querydsl.core.util.CloseableIterator;
import org.batoo.jpa.core.impl.criteria.QueryImpl;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.spi.ScrollableResultsImplementor;
import org.hibernate.transform.ResultTransformer;
import org.junit.Test;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.domain4.Library;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.Matchers.instanceOf;
import static org.hibernate.ScrollMode.FORWARD_ONLY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HibernateHandlerTest {

    private final HibernateHandler hibernateHandler = new HibernateHandler();
    private final NativeQuery nativeQuery = createMock(NativeQuery.class);
    private final String alias = "library";
    private final Class<Library> classType = Library.class;

    @Test
    public void should_add_entity() {
        expect(nativeQuery.unwrap(NativeQuery.class)).andReturn(nativeQuery);
        expect(nativeQuery.addEntity(alias, classType)).andReturn(nativeQuery);
        replay(nativeQuery);

        hibernateHandler.addEntity(nativeQuery, alias, classType);

        verify(nativeQuery);
    }

    @Test(expected = PersistenceException.class)
    public void addEntity_should_throw_persistence_exception_when_invalid_query_type() {
        Query notSupportedQuery = createMock(QueryImpl.class);
        PersistenceException expectedThrow =
            new PersistenceException(ExceptionLocalization.buildMessage("unable_to_unwrap_jpa",
                                                                        new String[]{Query.class.getName(), NativeQuery.class.getName()}));

        expect(notSupportedQuery.unwrap(NativeQuery.class)).andThrow(expectedThrow);
        replay(notSupportedQuery);

        hibernateHandler.addEntity(notSupportedQuery, alias, classType);
    }

    @Test
    public void should_add_scalar() {
        expect(nativeQuery.unwrap(NativeQuery.class)).andReturn(nativeQuery);
        expect(nativeQuery.addScalar(alias)).andReturn(nativeQuery);
        replay(nativeQuery);

        hibernateHandler.addScalar(nativeQuery, alias, classType);

        verify(nativeQuery);
    }

    @Test(expected = PersistenceException.class)
    public void addScalar_should_throw_persistence_exception_when_invalid_query_type() {
        Query notSupportedQuery = createMock(QueryImpl.class);
        PersistenceException expectedThrow =
            new PersistenceException(ExceptionLocalization.buildMessage("unable_to_unwrap_jpa",
                                                                        new String[]{Query.class.getName(), NativeQuery.class.getName()}));

        expect(notSupportedQuery.unwrap(NativeQuery.class)).andThrow(expectedThrow);
        replay(notSupportedQuery);

        hibernateHandler.addScalar(notSupportedQuery, alias, classType);
    }

    @Test
    public void should_get_false_when_check_native_query_type() {
        assertFalse(hibernateHandler.createNativeQueryTyped());
    }

    @Test
    public void should_get_true_when_check_wrap_entity_projections_for_hibernate_query_syntax_by_using_curly_braces() {
        assertTrue(hibernateHandler.wrapEntityProjections());
    }

    @Test
    public void should_return_transforming_iterator_when_call_iterate_function() {
        ScrollableResultsImplementor scrollableResultsImplementor = createMock(ScrollableResultsImplementor.class);
        FactoryExpression<?> factoryExpression = createMock(FactoryExpression.class);

        expect(nativeQuery.unwrap(org.hibernate.query.Query.class)).andReturn(nativeQuery);
        expect(nativeQuery.scroll(FORWARD_ONLY)).andReturn(scrollableResultsImplementor);
        replay(nativeQuery);

        assertThat(hibernateHandler.iterate(nativeQuery, factoryExpression), instanceOf(TransformingIterator.class));
    }

    @Test
    public void should_return_closeable_iterator_when_call_iterate_function() {
        Query query = createMock(Query.class);
        List queryResultList = createMock(List.class);
        Iterator iterator = createMock(Iterator.class);

        expect(query.unwrap(org.hibernate.query.Query.class)).andThrow(new PersistenceException("Cannot unwrap Query"));
        expect(query.getResultList()).andReturn(queryResultList);
        expect(queryResultList.iterator()).andReturn(iterator);
        replay(query);

        assertTrue(hibernateHandler.iterate(query, null) instanceof CloseableIterator);
    }

    @Test
    public void should_ReturnTransformingIterator_when_other_query_implementor() {
        Query query = createMock(Query.class);
        FactoryExpression<?> factoryExpression = createMock(FactoryExpression.class);
        List queryResultList = createMock(List.class);
        Iterator iterator = createMock(Iterator.class);

        expect(query.unwrap(org.hibernate.query.Query.class)).andThrow(new PersistenceException("Cannot unwrap Query"));
        expect(query.getResultList()).andReturn(queryResultList);
        expect(queryResultList.iterator()).andReturn(iterator);
        replay(query);

        assertEquals(TransformingIterator.class, hibernateHandler.iterate(query, factoryExpression).getClass());
    }

    @Test
    public void should_transform() {
        FactoryExpression<?> projection = createMock(FactoryExpression.class);

        expect(nativeQuery.unwrap(org.hibernate.query.Query.class)).andReturn(nativeQuery);
        expect(nativeQuery.setResultTransformer(anyObject(ResultTransformer.class))).andReturn(nativeQuery);
        replay(nativeQuery);

        assertTrue(hibernateHandler.transform(nativeQuery, projection));
    }

}
