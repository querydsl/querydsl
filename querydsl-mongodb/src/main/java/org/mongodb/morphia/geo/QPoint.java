package org.mongodb.morphia.geo;

import javax.annotation.Nullable;
import java.lang.reflect.AnnotatedElement;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.mongodb.morphia.MorphiaExpressions;

/**
 * {@code QPoint} represents Point paths, needed to support geo spatial querying features of Mongodb
 *
 * @author jmoghisi
 *
 */
public class QPoint extends SimpleExpression<Point> implements Path<Point> {

    private final PathImpl<Point> pathMixin;

    public QPoint(String var) {
        this(PathMetadataFactory.forVariable(var));
    }

    public QPoint(Path<?> parent, String property) {
        this(PathMetadataFactory.forProperty(parent, property));
    }

    public QPoint(PathMetadata metadata) {
        super(ExpressionUtils.path(Point.class, metadata));
        this.pathMixin = (PathImpl<Point>) mixin;
    }

    @Override
    public PathMetadata getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return pathMixin.getAnnotatedElement();
    }

    @Nullable
    @Override
    public <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
        return v.visit(pathMixin, context);
    }

    public BooleanExpression near(Point point) {
        return MorphiaExpressions.near(this, point);
    }

    public BooleanExpression nearSphere(Point point) {
        return MorphiaExpressions.nearSphere(this, point);
    }

}
