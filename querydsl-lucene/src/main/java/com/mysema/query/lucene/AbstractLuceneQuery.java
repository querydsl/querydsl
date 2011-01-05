package com.mysema.query.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections15.Transformer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.MatchAllDocsQuery;
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

public class AbstractLuceneQuery<T,Q extends AbstractLuceneQuery<T,Q>> implements SimpleQuery<Q>,
SimpleProjectable<T> {

    private final QueryMixin<Q> queryMixin;

    private final Searcher searcher;

    private final LuceneSerializer serializer;
    
    private final Transformer<Document, T> transformer;

    @SuppressWarnings("unchecked")
    public AbstractLuceneQuery(final LuceneSerializer serializer, final Searcher searcher,
                               final Transformer<Document, T> transformer) {
        queryMixin = new QueryMixin<Q>((Q) this);
        this.serializer = serializer;
        this.searcher = searcher;
        this.transformer = transformer;
    }

    public AbstractLuceneQuery(final Searcher searcher, final Transformer<Document, T> transformer) {
        this(LuceneSerializer.DEFAULT, searcher, transformer);
    }

    private long innerCount(){
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
    public long count() {
        return innerCount();
    }

    @Override
    public long countDistinct() {
        return innerCount();
    }

    private Query createQuery() {
        if (queryMixin.getMetadata().getWhere() == null) {
            return new MatchAllDocsQuery();
        }
        return serializer.toQuery(queryMixin.getMetadata().getWhere(),
                queryMixin.getMetadata());
    }

    @Override
    public Q distinct(){
        return queryMixin.distinct();
    }
    
    @Override
    public Q limit(final long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public CloseableIterator<T> iterate() {
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
                return new ResultIterator<T>(scoreDocs, offset, searcher, transformer);
            } else {
                return new EmptyCloseableIterator<T>();
            }
        } catch (final IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public CloseableIterator<T> iterateDistinct() {
        return iterate();
    }

    private List<T> innerList(){
        return new IteratorAdapter<T>(iterate()).asList();
    }

    @Override
    public List<T> list() {
        return innerList();
    }

    @Override
    public List<T> listDistinct() {
        return list();
    }

    @Override
    public SearchResults<T> listDistinctResults() {
        return listResults();
    }

    @Override
    public SearchResults<T> listResults() {
        List<T> documents = innerList();
        /*
         * TODO Get rid of count(). It could be implemented by iterating the
         * list results in list* from n to m.
         */
        return new SearchResults<T>(documents, queryMixin.getMetadata().getModifiers(), innerCount());
    }

    @Override
    public Q offset(final long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public Q orderBy(final OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public Q restrict(final QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public <P> Q set(final ParamExpression<P> param, final P value) {
        return queryMixin.set(param, value);
    }

    @Override
    public T uniqueResult() {
        try {
            int maxDoc = searcher.maxDoc();
            if (maxDoc == 0) {
                return null;
            }
            final ScoreDoc[] scoreDocs = searcher.search(createQuery(), maxDoc).scoreDocs;
            if (scoreDocs.length > 1) {
                throw new QueryException("More than one result found!");
            } else if (scoreDocs.length == 1) {
                return transformer.transform(searcher.doc(scoreDocs[0].doc));
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public Q where(final Predicate... e) {
        return queryMixin.where(e);
    }

    
}
