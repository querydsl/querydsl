/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import com.mysema.query.QueryException;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;

/**
 * @author tiwe
 *
 * @param <Document>
 */
public class LuceneQuery implements SimpleQuery<LuceneQuery>, SimpleProjectable<Document>{

//    private final Path<?> entityPath;

    private final QueryMixin<LuceneQuery> queryMixin;

    private final LuceneSerializer serializer;

    private final Searcher searcher;

    // TODO Is there an alternative for this?
    private static final int MAX_RESULT_COUNT = 30000;

    public LuceneQuery(Path<?> entityPath, LuceneSerializer serializer, Searcher searcher) {
//        this.entityPath = entityPath;
        this.queryMixin = new QueryMixin<LuceneQuery>(this);
        this.serializer = serializer;
        this.searcher = searcher;
    }

    @Override
    public LuceneQuery limit(long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public LuceneQuery offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public LuceneQuery orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public LuceneQuery restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public LuceneQuery where(EBoolean... e) {
        return queryMixin.where(e);
    }

    private Query createQuery() {
        return serializer.toQuery(queryMixin.getMetadata().getWhere());
    }

    @Override
    public long count() {
        try {
            return searcher.search(createQuery(), MAX_RESULT_COUNT).totalHits;
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public long countDistinct() {
        queryMixin.getMetadata().setDistinct(true);
        return count();
    }

    @Override
    public List<Document> list() {
        List<OrderSpecifier<?>> orderBys = queryMixin.getMetadata().getOrderBy();
        if (!orderBys.isEmpty()) {
            return listSorted(orderBys);
        }

        List<Document> documents = new ArrayList<Document>();
        try {
            ScoreDoc[] scoreDocs = searcher.search(createQuery(), MAX_RESULT_COUNT).scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }
        } catch (IOException e) {
            throw new QueryException(e);
        }
        return documents;
    }

    private List<Document> listSorted(List<OrderSpecifier<?>> orderBys) {
        List<Document> documents = new ArrayList<Document>();
        List<SortField> sortFields = new ArrayList<SortField>();
        for (OrderSpecifier<?> orderSpecifier : orderBys) {
            if (!(orderSpecifier.getTarget() instanceof Path<?>)) {
                throw new IllegalArgumentException("argument was not of type Path.");
            }
            sortFields.add(new SortField(toField((Path<?>)orderSpecifier.getTarget()), Locale.ENGLISH, !orderSpecifier.isAscending()));
        }
        Sort sort = new Sort();
        sort.setSort(sortFields.toArray(new SortField[sortFields.size()]));
        try {
            ScoreDoc[] scoreDocs = searcher.search(createQuery(), null, MAX_RESULT_COUNT, sort).scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }
        } catch (IOException e) {
            throw new QueryException(e);
        }
        return documents;
    }

    @Override
    public List<Document> listDistinct() {
        queryMixin.getMetadata().setDistinct(true);
        return list();
    }

    @Override
    public SearchResults<Document> listDistinctResults() {
        queryMixin.getMetadata().setDistinct(true);
        return listResults();
    }

    @Override
    public SearchResults<Document> listResults() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document uniqueResult() {
        try {
            ScoreDoc[] scoreDocs = searcher.search(createQuery(), MAX_RESULT_COUNT).scoreDocs;
            if (scoreDocs.length > 1) {
                throw new QueryException("More than one result found!");
            }
            return searcher.doc(scoreDocs[0].doc);
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    public String toField(Path<?> path) {
        return path.getMetadata().getExpression().toString();
    }

}
