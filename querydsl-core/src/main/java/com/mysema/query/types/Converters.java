/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static com.mysema.query.types.StringEscape.escapeForLike;

import org.apache.commons.collections15.Transformer;

/**
 * @author tiwe
 *
 */
public final class Converters {
    
    private Converters(){}
    
    public static final Transformer<Expression<String>,Expression<String>> toLowerCase = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return new ConstantImpl<String>(arg.toString().toLowerCase());
            }else{
                return new OperationImpl<String>(String.class, Ops.LOWER, arg);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toUpperCase = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return new ConstantImpl<String>(arg.toString().toUpperCase());
            }else{
                return new OperationImpl<String>(String.class, Ops.UPPER, arg);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toStartsWithViaLike = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return new ConstantImpl<String>(escapeForLike((Constant<String>)arg) + "%"); 
            }else{
                return new OperationImpl<String>(String.class, Ops.CONCAT, arg, new ConstantImpl<String>("%"));
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toStartsWithViaLikeLower = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return new ConstantImpl<String>(escapeForLike((Constant<String>)arg).toLowerCase() + "%"); 
            }else{
                Expression<String> concated = new OperationImpl<String>(String.class, Ops.CONCAT, arg, new ConstantImpl<String>("%"));
                return new OperationImpl<String>(String.class, Ops.LOWER, concated);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toEndsWithViaLike = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return new ConstantImpl<String>("%" + escapeForLike((Constant<String>)arg)); 
            }else{
                return new OperationImpl<String>(String.class, Ops.CONCAT, new ConstantImpl<String>("%"), arg);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toEndsWithViaLikeLower = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return new ConstantImpl<String>("%" + escapeForLike((Constant<String>)arg).toLowerCase()); 
            }else{
                Expression<String> concated = new OperationImpl<String>(String.class, Ops.CONCAT, new ConstantImpl<String>("%"), arg);
                return new OperationImpl<String>(String.class, Ops.LOWER, concated);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toContainsViaLike = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return new ConstantImpl<String>("%" + escapeForLike((Constant<String>)arg) + "%"); 
            }else{
                Expression<String> concated = new OperationImpl<String>(String.class, Ops.CONCAT, new ConstantImpl<String>("%"), arg);
                return new OperationImpl<String>(String.class, Ops.CONCAT, concated, new ConstantImpl<String>("%"));
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toContainsViaLikeLower = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return new ConstantImpl<String>("%" + escapeForLike((Constant<String>)arg).toLowerCase() + "%"); 
            }else{
                Expression<String> concated = new OperationImpl<String>(String.class, Ops.CONCAT, new ConstantImpl<String>("%"), arg);
                concated = new OperationImpl<String>(String.class, Ops.CONCAT, concated, new ConstantImpl<String>("%"));
                return new OperationImpl<String>(String.class, Ops.LOWER, concated);
            }
        }
    };

}
