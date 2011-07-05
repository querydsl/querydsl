package com.mysema.query.support;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.query.SimpleSubQuery;
import com.mysema.query.types.template.BooleanTemplate;
import com.mysema.query.types.template.ComparableTemplate;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.query.types.template.SimpleTemplate;
import com.mysema.query.types.template.StringTemplate;

/**
 * Expression factory class
 * 
 * @author tiwe
 *
 */
public final class Expressions {

    @Nullable
    public static BooleanExpression allOf(BooleanExpression... exprs){
        return BooleanExpression.allOf(exprs);
    }

    @Nullable
    public static BooleanExpression anyOf(BooleanExpression... exprs){
        return BooleanExpression.anyOf(exprs);
    }
        
    public static <T> Expression<T> constant(T value) {
        return new ConstantImpl<T>(value);    
    }
    
    public static <T> SimpleExpression<T> template(Class<T> cl, String template, Expression<?>... args) {
        return SimpleTemplate.create(cl, template, args);
    }
    
    public static <T extends Comparable<?>> ComparableExpression<T> comparableTemplate(Class<T> cl, String template, Expression<?>... args) {
        return ComparableTemplate.create(cl, template, args);
    }
    
    public static <T extends Number & Comparable<?>> NumberExpression<T> numberTemplate(Class<T> cl, String template, Expression<?>... args) {
        return NumberTemplate.create(cl, template, args);
    }
    
    public static StringExpression stringTemplate(String template, Expression<?>... args) {
        return StringTemplate.create(template, args);
    }
    
    public static BooleanExpression booleanTemplate(String template, Expression<?>... args) {
        return BooleanTemplate.create(template, args);
    }
    
    public static <T> SimpleExpression<T> subQuery(Class<T> type, QueryMetadata metadata) {
        return new SimpleSubQuery<T>(type, metadata);
    }

    public static <T> SimpleExpression<T> operation(Class<T> type, Operator<? super T> operator, Expression<?>... args) {
        return SimpleOperation.create(type, operator, args);
    }

    public static <T> SimplePath<T> path(Class<T> type, String variable) {
        return new SimplePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    public static <T> SimplePath<T> path(Class<T> type, Path<?> parent, String property) {
        return new SimplePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }    
        
    private Expressions(){}

}
