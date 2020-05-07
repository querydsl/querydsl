package com.querydsl.jpa;

import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.query.spi.ScrollableResultsImplementor;
import org.hibernate.transform.ResultTransformer;
import org.junit.Test;

import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.domain4.Library;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hibernate.ScrollMode.FORWARD_ONLY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HibernateHandlerTest {

    private final HibernateHandler hibernateHandler = new HibernateHandler();
    private final NativeQuery nativeQueryFromJpaSqlQuery = createMock(NativeQuery.class);
    final QueryImpl queryImplFromJpaQuery = createMock(org.hibernate.query.internal.QueryImpl.class);
    private final String alias = "library";
    private final Class<Library> classType = Library.class;

    @Test
    public void should_add_entity() {
        expect(nativeQueryFromJpaSqlQuery.unwrap(NativeQuery.class)).andReturn(nativeQueryFromJpaSqlQuery);
        expect(nativeQueryFromJpaSqlQuery.addEntity(alias, classType)).andReturn(nativeQueryFromJpaSqlQuery);
        replay(nativeQueryFromJpaSqlQuery);

        hibernateHandler.addEntity(nativeQueryFromJpaSqlQuery, alias, classType);

        verify(nativeQueryFromJpaSqlQuery);
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
        expect(nativeQueryFromJpaSqlQuery.unwrap(NativeQuery.class)).andReturn(nativeQueryFromJpaSqlQuery);
        expect(nativeQueryFromJpaSqlQuery.addScalar(alias)).andReturn(nativeQueryFromJpaSqlQuery);
        replay(nativeQueryFromJpaSqlQuery);

        hibernateHandler.addScalar(nativeQueryFromJpaSqlQuery, alias, classType);

        verify(nativeQueryFromJpaSqlQuery);
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
    public void should_return_transforming_iterator_when_call_iterate_by_using_native_query() {
        ScrollableResultsImplementor scrollableResultsImplementor = createMock(ScrollableResultsImplementor.class);
        FactoryExpression<?> factoryExpression = createMock(FactoryExpression.class);

        expect(nativeQueryFromJpaSqlQuery.unwrap(NativeQuery.class)).andReturn(nativeQueryFromJpaSqlQuery);
        expect(nativeQueryFromJpaSqlQuery.scroll(FORWARD_ONLY)).andReturn(scrollableResultsImplementor);

        assertEquals(TransformingIterator.class, hibernateHandler.iterate(nativeQueryFromJpaSqlQuery, factoryExpression).getClass());
    }

    @Test
    public void should_return_transforming_iterator_when_call_iterate_function_by_using_query_impl() {
        ScrollableResultsImplementor scrollableResultsImplementor = createMock(ScrollableResultsImplementor.class);
        FactoryExpression<?> factoryExpression = createMock(FactoryExpression.class);

        expect(queryImplFromJpaQuery.unwrap(QueryImpl.class)).andReturn(queryImplFromJpaQuery);
        expect(queryImplFromJpaQuery.scroll(FORWARD_ONLY)).andReturn(scrollableResultsImplementor);

        assertEquals(TransformingIterator.class, hibernateHandler.iterate(queryImplFromJpaQuery, factoryExpression).getClass());
    }

    @Test
    public void should_return_iterator_adapter_when_call_iterate_function() {
        Query query = createMock(Query.class);
        List queryResultList = createMock(List.class);
        Iterator iterator = createMock(Iterator.class);

        expect(query.getResultList()).andReturn(queryResultList);
        expect(queryResultList.iterator()).andReturn(iterator);
        replay(query);

        assertEquals(IteratorAdapter.class, hibernateHandler.iterate(query, null).getClass());
    }

    @Test
    public void should_return_transforming_iterator_when_other_query_implementor() {
        Query query = createMock(Query.class);
        FactoryExpression<?> factoryExpression = createMock(FactoryExpression.class);
        List queryResultList = createMock(List.class);
        Iterator iterator = createMock(Iterator.class);

        expect(query.getResultList()).andReturn(queryResultList);
        expect(queryResultList.iterator()).andReturn(iterator);
        replay(query);

        assertEquals(TransformingIterator.class, hibernateHandler.iterate(query, factoryExpression).getClass());
    }

    @Test
    public void should_transform() {
        final FactoryExpression<?> projection = createMock(FactoryExpression.class);

        expect(queryImplFromJpaQuery.unwrap(org.hibernate.query.Query.class)).andReturn(nativeQueryFromJpaSqlQuery);
        expect(queryImplFromJpaQuery.setResultTransformer(anyObject(ResultTransformer.class))).andReturn(queryImplFromJpaQuery);
        replay(queryImplFromJpaQuery);

        assertTrue(hibernateHandler.transform(queryImplFromJpaQuery, projection));
    }

}
