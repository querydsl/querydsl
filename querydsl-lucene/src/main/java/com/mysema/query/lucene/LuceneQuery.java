/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.EmptyCloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Predicate;

/**
 * LuceneQuery is a Querydsl query implementation for Lucene queries.
 * 
 * @author vema
 */
public class LuceneQuery implements SimpleQuery<LuceneQuery>,
        SimpleProjectable<Document> {

    private final QueryMixin<LuceneQuery> queryMixin;

    private final Searcher searcher;

    private final LuceneSerializer serializer;

    public LuceneQuery(final LuceneSerializer serializer,
            final Searcher searcher) {
        queryMixin = new QueryMixin<LuceneQuery>(this);
        this.serializer = serializer;
        this.searcher = searcher;
    }

    public LuceneQuery(final Searcher searcher) {
        this(LuceneSerializer.DEFAULT, searcher);
    }

    @Override
    public long count() {
        try {
            final int maxDoc = searcher.maxDoc();
            if (maxDoc == 0) {
                return 0;
            }
            return searcher.search(createQuery(), maxDoc).totalHits;
        } catch (final IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public long countDistinct() {
        return count();
    }

    private Query createQuery() {
        if (queryMixin.getMetadata().getWhere() == null) {
            throw new QueryException("Where clause was null.");
        }
        return serializer.toQuery(queryMixin.getMetadata().getWhere(),
                queryMixin.getMetadata());
    }

    @Override
    public LuceneQuery limit(final long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public CloseableIterator<Document> iterate() {
        final QueryMetadata metadata = queryMixin.getMetadata();
        final List<OrderSpecifier<?>> orderBys = metadata.getOrderBy();
        final Long queryLimit = metadata.getModifiers().getLimit();
        final Long queryOffset = metadata.getModifiers().getOffset();
        Sort sort = null;
        int limit;
        final int offset = queryOffset != null ? queryOffset.intValue() : 0;
        try {
            limit = searcher.maxDoc();
        } catch (final IOException e) {
            throw new QueryException(e);
        }
        if (queryLimit != null && queryLimit.intValue() < limit) {
            limit = queryLimit.intValue();
        }
        if (!orderBys.isEmpty()) {
            sort = serializer.toSort(orderBys);
        }

        try {
            ScoreDoc[] scoreDocs;
            if (sort != null) {
                scoreDocs = searcher.search(createQuery(), null,
                        limit + offset, sort).scoreDocs;
            } else {
                scoreDocs = searcher.search(createQuery(), limit + offset).scoreDocs;
            }
            if (offset < scoreDocs.length) {
                return new DocumentIterator(scoreDocs, offset, searcher);
            } else {
                return new EmptyCloseableIterator<Document>();
            }
        } catch (final IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public List<Document> list() {
        return new IteratorAdapter<Document>(iterate()).asList();
    }

    @Override
    public List<Document> listDistinct() {
        return list();
    }

    @Override
    public SearchResults<Document> listDistinctResults() {
        return listResults();
    }

    @Override
    public SearchResults<Document> listResults() {
        final List<Document> documents = list();
        /*
         * TODO Get rid of count(). It could be implemented by iterating the
         * list results in list* from n to m.
         */
        return new SearchResults<Document>(documents, queryMixin.getMetadata().getModifiers(), count());
    }

    @Override
    public LuceneQuery offset(final long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public LuceneQuery orderBy(final OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public LuceneQuery restrict(final QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public <T> LuceneQuery set(final ParamExpression<T> param, final T value) {
        return queryMixin.set(param, value);
    }

    @Override
    public Document uniqueResult() {
        try {
            final int maxDoc = searcher.maxDoc();
            if (maxDoc == 0) {
                return null;
            }
            final ScoreDoc[] scoreDocs = searcher.search(createQuery(), maxDoc).scoreDocs;
            if (scoreDocs.length > 1) {
                throw new QueryException("More than one result found!");
            } else if (scoreDocs.length == 1) {
                return searcher.doc(scoreDocs[0].doc);
            } else {
                return null;
            }
        } catch (final IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public LuceneQuery where(final Predicate... e) {
        return queryMixin.where(e);
    }

}
