package org.mongodb.morphia.geo;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.mongodb.morphia.MorphiaExpressions;

import javax.annotation.Nullable;

/**
 * {@code QPoint} is an adapter type for Point to use geo spatial querying features of Mongodb
 *
 * @author jmoghisi
 *
 */
public class QPoint extends EntityPathBase<Point> {

    public QPoint(String variable) {
        super(Point.class, variable);
    }

    public QPoint(PathMetadata metadata) {
        super(Point.class, metadata);
    }

    public QPoint(PathMetadata metadata, @Nullable PathInits inits) {
        super(Point.class, metadata, inits);
    }

    public BooleanExpression near(Point point) {
        return MorphiaExpressions.near(this, point);
    }

    public BooleanExpression nearSphere(Point point) {
        return MorphiaExpressions.nearSphere(this, point);
    }
}
