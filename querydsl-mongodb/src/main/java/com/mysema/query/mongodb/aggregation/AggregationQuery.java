/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysema.query.mongodb.aggregation;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.mongodb.MongodbSerializer;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

/**
 * This class performs aggregation operations to deliver the results of
 * operations
 *
 * @author Komi Innocent
 */
public class AggregationQuery<T> {

    DBCollection collection;
    Function<DBObject, T> transformer;
    MongodbSerializer serializer;
    private final QueryMixin<AggregationQuery<T>> queryMixin;
    List<DBObject> pipeline = new LinkedList<DBObject>();
    List<AggregationOperation<T>> operations;

    public AggregationQuery(DBCollection collection, Function<DBObject, T> transformer, MongodbSerializer serializer, AggregationOperation<T>... op) {
        this.queryMixin = new QueryMixin<AggregationQuery<T>>(this, new DefaultQueryMetadata().noValidate(), false);
        this.transformer = transformer;
        this.collection = collection;
        this.serializer = serializer;
        this.operations = Arrays.asList(op);
        createPipeline(operations);
    }

    /**
     *
     * @param aggregationOperations
     */
    private void createPipeline(List<AggregationOperation<T>> aggregationOperations) {
        for (AggregationOperation<T> operation : aggregationOperations) {
            pipeline.add(operation.toDBObject(serializer));
        }
    }

    /**
     *
     * @return return a list of T from the aggregation operation
     */
    public List<T> list() {
        try {
            Cursor cursor = createCursor();
            List<T> results = new ArrayList<T>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                results.add(transformer.apply(dbObject));
            }
            return results;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    /**
     *
     * @return return a list of DBObject from the aggregation operation
     */
    public List<DBObject> listResults() {
        try {
            Cursor cursor = createCursor();
            List<DBObject> results = new ArrayList<DBObject>();
            while (cursor.hasNext()) {
                results.add(cursor.next());
            }
            return results;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    protected Cursor createCursor() {
        AggregationOptions aggregationOptions = AggregationOptions.builder()
                .batchSize(100)
                .outputMode(AggregationOptions.OutputMode.CURSOR)
                .allowDiskUse(true)
                .build();
        Cursor cursor = collection.aggregate(pipeline, aggregationOptions);
        return cursor;
    }

}
