/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

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
                return ConstantImpl.create(arg.toString().toLowerCase());
            }else{
                return new OperationImpl<String>(String.class, Ops.LOWER, arg);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toUpperCase = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return ConstantImpl.create(arg.toString().toUpperCase());
            }else{
                return new OperationImpl<String>(String.class, Ops.UPPER, arg);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toStartsWithViaLike = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return ConstantImpl.create(escapeForLike((Constant<String>)arg) + "%"); 
            }else{
                return new OperationImpl<String>(String.class, Ops.CONCAT, arg, ConstantImpl.create("%"));
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toStartsWithViaLikeLower = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return ConstantImpl.create(escapeForLike((Constant<String>)arg).toLowerCase() + "%"); 
            }else{
                Expression<String> concated = new OperationImpl<String>(String.class, Ops.CONCAT, arg, ConstantImpl.create("%"));
                return new OperationImpl<String>(String.class, Ops.LOWER, concated);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toEndsWithViaLike = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return ConstantImpl.create("%" + escapeForLike((Constant<String>)arg)); 
            }else{
                return new OperationImpl<String>(String.class, Ops.CONCAT, ConstantImpl.create("%"), arg);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toEndsWithViaLikeLower = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return ConstantImpl.create("%" + escapeForLike((Constant<String>)arg).toLowerCase()); 
            }else{
                Expression<String> concated = new OperationImpl<String>(String.class, Ops.CONCAT, ConstantImpl.create("%"), arg);
                return new OperationImpl<String>(String.class, Ops.LOWER, concated);
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toContainsViaLike = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return ConstantImpl.create("%" + escapeForLike((Constant<String>)arg) + "%"); 
            }else{
                Expression<String> concated = new OperationImpl<String>(String.class, Ops.CONCAT, ConstantImpl.create("%"), arg);
                return new OperationImpl<String>(String.class, Ops.CONCAT, concated, ConstantImpl.create("%"));
            }
        }
    };

    public static final Transformer<Expression<String>,Expression<String>> toContainsViaLikeLower = new Transformer<Expression<String>,Expression<String>>(){
        @Override
        public Expression<String> transform(Expression<String> arg) {
            if (arg instanceof Constant<?>){
                return ConstantImpl.create("%" + escapeForLike((Constant<String>)arg).toLowerCase() + "%"); 
            }else{
                Expression<String> concated = new OperationImpl<String>(String.class, Ops.CONCAT, ConstantImpl.create("%"), arg);
                concated = new OperationImpl<String>(String.class, Ops.CONCAT, concated, ConstantImpl.create("%"));
                return new OperationImpl<String>(String.class, Ops.LOWER, concated);
            }
        }
    };
    
    public static String escapeForLike(Constant<String> expr){
        String str = expr.getConstant();
        if (str.contains("%") || str.contains("_")){
            str = str.replace("%", "\\%").replace("_", "\\_");
        }
        return str;
    }

}
