/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysema.query.mongodb.aggregation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mysema.query.mongodb.MongodbSerializer;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.bson.BSONObject;

/**
 *  An aggregation projection operation
 * @author Komi Innocent <komi.innocent@gmail.com>
 */
public class ProjectOperation<T> implements AggregationOperation<T> {

    List<Path<T>> projectionsPath = new LinkedList<Path<T>>();
    List<Expression<T>> projectionsExpressions = new LinkedList<Expression<T>>();

    public ProjectOperation(Path<T>... paths) {
        if (paths != null) {
            projectionsPath.addAll(Arrays.asList(paths));
        }
    }

    public ProjectOperation<T> expression(Expression<T>... expressions) {
        projectionsExpressions.addAll(Arrays.asList(expressions));
        return this;
    }

    @Override
    public DBObject toDBObject(MongodbSerializer serializer) {
        BasicDBObject fieldObject = new BasicDBObject();

        for (Path<T> path : projectionsPath) {
            fieldObject.put(path.getMetadata().getName(), 1);
        }

        if (projectionsExpressions != null) {
            for (Expression<T> expression : projectionsExpressions) {
                fieldObject.putAll((BSONObject) serializer.handle(expression));
            }
        }

        return new BasicDBObject("$project", fieldObject);
    }

}
