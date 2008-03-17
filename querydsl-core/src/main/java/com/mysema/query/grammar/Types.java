/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;


import static com.mysema.query.grammar.PathMetadata.*;

import java.util.Collection;
import java.util.Map;

import com.mysema.query.grammar.Ops.Op;

/**
 * Types provides the types of the fluent grammar
 *
 * @author tiwe
 * @version $Id$
 */
public class Types {
    
    public static class AliasEntity<D> extends ExprEntity<D> implements AliasToPath{
        private final Expr<?> from;
        private final Path<?> to;
        AliasEntity(PathEntity<D> from, PathEntity<D> to) {
            super(from.getType());
            this.from = from;
            this.to = to;
        }
        public Expr<?> getFrom() {return from;}
        public  Path<?> getTo() {return to;}  
    }
    
    public static class AliasEntityCollection<D> extends ExprEntity<D> implements AliasToPath{
        private final Expr<?> from;
        private final Path<?> to;
        AliasEntityCollection(PathEntityCollection<D> from, Path<D> to) {
            super(null);
            this.from = from;
            this.to = to;
        }
        public Expr<?> getFrom() {return from;}
        public  Path<?> getTo() {return to;}        
    }
            
    public static class AliasNoEntity<D> extends ExprNoEntity<D> implements AliasSimple{     
        private final Expr<?> from;
        private final String to;
        AliasNoEntity(Expr<D> from, String to) {
            super(from.getType());
            this.from = from;
            this.to = to;
        }
        public Expr<D> as(String to) {
            return Grammar.as(this, to);
        }   
        public Expr<?> getFrom() {return from;}
        public String getTo() {return to;}  
    }
        
    public interface AliasSimple{ 
        Expr<?> getFrom();
        String getTo();
    }
    
    public interface AliasToPath{
        Expr<?> getFrom();
        Path<?> getTo();
    }
    
    public static class ConstantExpr<D> extends Expr<D>{
        private final D constant;
        @SuppressWarnings("unchecked")
        ConstantExpr(D constant) {
            super((Class<D>) constant.getClass());
            this.constant = constant;
        }
        public D getConstant(){ return constant;}
        @Override public String toString(){ return constant.toString(); }
    }
    
    public static abstract class Expr<D>{
        private final Class<D> type;
        Expr(Class<D> type){this.type = type;}        
        public <B extends D> ExprBoolean eq(B right){return Grammar.eq(this, right);}        
        public <B extends D> ExprBoolean eq(Expr<B> right){return Grammar.eq(this, right);}
        public Class<D> getType(){ return type;}
        public <B extends D> ExprBoolean ne(B right){return Grammar.ne(this, right);}
        public <B extends D> ExprBoolean ne(Expr<B> right){return Grammar.ne(this, right);}
    }
    
    public static abstract class ExprBoolean extends ExprNoEntity<Boolean>{
        ExprBoolean() {super(Boolean.class);}
        public ExprBoolean and(ExprBoolean right) {return Grammar.and(this, right);}
        public ExprBoolean or(ExprBoolean right) {return Grammar.or(this, right);}
    }
    
    public static abstract class ExprComparable<D extends Comparable<D>> extends ExprNoEntity<D>{
        ExprComparable(Class<D> type) {super(type);}
        public ExprBoolean after(D right) {return Grammar.after(this,right);}
        public ExprBoolean after(Expr<D> right) {return Grammar.after(this,right);}  
        public OrderSpecifier<D> asc() {return Grammar.asc(this);}
        public ExprBoolean before(D right) {return Grammar.before(this,right);}        
        public ExprBoolean before(Expr<D> right) {return Grammar.before(this,right);}
        public ExprBoolean between(D first, D second) {return Grammar.between(this,first,second);}       
        public ExprBoolean between(Expr<D> first, Expr<D> second) {return Grammar.between(this,first,second);}  
        public OrderSpecifier<D> desc() {return Grammar.desc(this);}        
        public ExprBoolean goe(D right) {return Grammar.goe(this,right);}  
        public ExprBoolean goe(Expr<D> right) {return Grammar.goe(this,right);}         
        public ExprBoolean gt(D right) {return Grammar.gt(this,right);}  
        public ExprBoolean gt(Expr<D> right) {return Grammar.gt(this,right);}
        public ExprBoolean in(D... args) {return Grammar.in(this,args);}
        public ExprBoolean in(CollectionType<D> arg) {return Grammar.in(this, arg);}
        public ExprBoolean loe(D right) {return Grammar.loe(this,right);}
        public ExprBoolean loe(Expr<D> right) {return Grammar.loe(this,right);}
        public ExprBoolean lt(D right) {return Grammar.lt(this,right);}  
        public ExprBoolean lt(Expr<D> right) {return Grammar.lt(this,right);}
        public ExprBoolean notBetween(D first, D second) {return Grammar.notBetween(this, first, second);}
        public ExprBoolean notBetween(Expr<D> first, Expr<D> second) {return Grammar.notBetween(this,first,second);}
        public ExprBoolean notIn(D...args) {return Grammar.notIn(this, args);}
        public ExprBoolean notIn(CollectionType<D> arg) {return Grammar.notIn(this, arg);}
    }
    
//    *, 
//    /, 
//    DIV, 
//    %, 
//    MOD
//    -, 
//    +
    
    public static abstract class ExprEntity<D> extends Expr<D>{
        ExprEntity(Class<D> type) {super(type);}        
    }        
            
    public static abstract class ExprNoEntity<D> extends Expr<D>{
        ExprNoEntity(Class<D> type) {super(type);}
        public Expr<D> as(String to){return Grammar.as(this, to);}
    }
        
    public static abstract class ExprString extends ExprComparable<String>{
        ExprString() {super(String.class);}
        public ExprString concat(Expr<String> str) {return Grammar.concat(this, str);}
        public ExprString concat(String str) {return Grammar.concat(this, str);}
        public ExprBoolean like(String str) { return Grammar.like(this, str); }
        public ExprString lower() { return Grammar.lower(this); }
        public ExprString substring(int beginIndex) { return Grammar.substring(this, beginIndex);}
        public ExprString substring(int beginIndex, int endIndex) { return Grammar.substring(this, beginIndex, endIndex);}
        public ExprString trim() { return Grammar.trim(this); }
        public ExprString upper() { return Grammar.upper(this); }
    }  

    public interface Operation<OP,RT>{
        Expr<?>[] getArgs();
        Op<OP> getOperator();
    }
    
    public static class OperationBoolean extends ExprBoolean implements Operation<Boolean,Boolean>{
        private final Expr<?>[] args;
        private final Op<Boolean> op;
        OperationBoolean(Op<Boolean> op, Expr<?>... args){
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<Boolean> getOperator() {return op;}    
    }    
    
    public static class OperationComparable<OpType,D extends Comparable<D>> extends ExprComparable<D> implements Operation<OpType,D> {
        private final Expr<?>[] args;
        private final Op<OpType> op;
        OperationComparable(Op<OpType> op, Expr<?>... args){
            super(null);
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<OpType> getOperator() {return op;}    
    }
        
    public static class OperationNumber<N extends Number,D extends Comparable<D>> extends OperationComparable<N,D>{
        OperationNumber(Op<N> op, Expr<?>[] args) {
            super(op, args);
        }        
    }
    
    public static class OperationString extends ExprString implements Operation<String,String>{
        private final Expr<?>[] args;
        private final Op<String> op;
        OperationString(Op<String> op, Expr<?>... args){
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<String> getOperator() {return op;}    
    }
    
    public interface Path<C>{
        PathMetadata<?> getMetadata();
        ExprBoolean isnotnull();
        ExprBoolean isnull();
    }
            
    public static class PathBoolean extends ExprBoolean implements PathNoEntity<Boolean>{
        private final PathMetadata<String> metadata;
        PathBoolean(PathMetadata<String> metadata) {
            this.metadata = metadata;
        }        
        public PathMetadata<String> getMetadata() {return metadata;}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}          
    }
    
    public interface CollectionType<D>{
        
    }
    
    public interface PathCollection<D> extends Path<Collection<D>>, CollectionType<D>{
        Expr<D> get(Expr<Integer> index);        
        Expr<D> get(int index);
        Expr<D> maxelement();
        Expr<D> minelement();
        ExprComparable<Integer> size();
    }
    
    public static class PathComparable<D extends Comparable<D>> extends ExprComparable<D> implements PathNoEntity<D>{
        private final PathMetadata<String> metadata;
        public PathComparable(Class<D> type, PathMetadata<String> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public PathMetadata<String> getMetadata() {return metadata;}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
    }
    
    public static class PathComponentCollection<D> extends ExprNoEntity<Collection<D>> implements PathCollection<D>{
        private final PathMetadata<String> metadata;
        private final Class<D> type;
        PathComponentCollection(Class<D> type, PathMetadata<String> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
        }        
        public ExprNoEntity<D> get(Expr<Integer> index) {
            return new PathNoEntitySimple<D>(type, forListAccess(this, index));
        }
        public ExprNoEntity<D> get(int index) {
            return new PathNoEntitySimple<D>(type, forListAccess(this, index));
        }
        public PathMetadata<String> getMetadata() {return metadata;}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
        public ExprEntity<D> maxelement() {
            return new PathEntity<D>(type, forMaxElement(this)); 
        }
        public ExprEntity<D> minelement() {
            return new PathEntity<D>(type, forMinElement(this)); 
        }
        public ExprComparable<Integer> size() { return Grammar.size(this);}
    }
    
    public static class PathComponentMap<K,V> extends ExprNoEntity<Map<K,V>> implements PathMap<K,V>{
        private final PathMetadata<String> metadata;
        private final Class<V> type;
        PathComponentMap(Class<V> type, PathMetadata<String> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
        }
        public ExprNoEntity<V> get(Expr<K> key) { 
            return new PathNoEntitySimple<V>(type, forMapAccess(this, key));
        }
        public ExprNoEntity<V> get(K key) { 
            return new PathNoEntitySimple<V>(type, forMapAccess(this, key));
        }
        public PathMetadata<String> getMetadata() {return metadata;}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
    }
    
    public static class PathEntity<D> extends ExprEntity<D> implements Path<D>{
        private final PathMetadata<?> metadata;        
        public PathEntity(Class<D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public PathEntity(Class<D> type, String localName) {
            super(type);
            metadata = forVariable(localName);
        }
        protected PathBoolean _boolean(String path){
            return new PathBoolean(forProperty(this, path));
        }
        protected <A extends Comparable<A>> PathComparable<A> _comparable(String path,Class<A> type) {
            return new PathComparable<A>(type, forProperty(this, path));
        }
        protected <A> PathEntityRenamable<A> _entity(String path, Class<A> type){
            return new PathEntityRenamable<A>(type, forProperty(this, path)); 
        }        
        protected <A>PathEntityCollection<A> _entitycol(String path,Class<A> type) {
            return new PathEntityCollection<A>(type, forProperty(this, path));
        }
        protected <A> PathNoEntitySimple<A> _simple(String path, Class<A> type){
            return new PathNoEntitySimple<A>(type, forProperty(this, path));
        }
        protected <A> PathComponentCollection<A> _simplecol(String path,Class<A> type) {
            return new PathComponentCollection<A>(type, forProperty(this, path));
        }        
        protected <K,V> PathComponentMap<K,V> _simplemap(String path, Class<K> key, Class<V> value){
            return new PathComponentMap<K,V>(value, forProperty(this, path));
        }
        protected PathString _string(String path){
            return new PathString(forProperty(this, path));
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public ExprBoolean in(CollectionType<D> right){return Grammar.in(this, right);}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
        public <B extends D> ExprBoolean typeOf(Class<B> type) {return Grammar.typeOf(this, type);}
    }
    
    public static class PathEntityCollection<D> extends ExprEntity<Collection<D>> implements PathCollection<D>{
        private final PathMetadata<String> metadata;
        private final Class<D> type;
        PathEntityCollection(Class<D> type, PathMetadata<String> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
        }        
        public AliasEntityCollection<D> as(PathEntity<D> to) {return Grammar.as(this, to);}
        public ExprEntity<D> get(Expr<Integer> index) {
            return new PathEntity<D>(type, forListAccess(this,index));
        }
        public ExprEntity<D> get(int index) {
            return new PathEntity<D>(type, forListAccess(this,index));
        }
        public PathMetadata<String> getMetadata() {return metadata;}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}    
        public ExprEntity<D> maxelement() {
            return new PathEntity<D>(type, forMaxElement(this)); 
        }
        public ExprEntity<D> minelement() {
            return new PathEntity<D>(type, forMinElement(this)); 
        }
        public ExprComparable<Integer> size() { return Grammar.size(this); }
    }
    
    public static class PathEntityMap<K,V> extends ExprEntity<Map<K,V>> implements PathMap<K,V>{
        private final PathMetadata<String> metadata;
        private final Class<V> type;
        PathEntityMap(Class<V> type, PathMetadata<String> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
        } 
        public ExprEntity<V> get(Expr<K> key) { 
            return new PathEntity<V>(type, forMapAccess(this, key));
        }
        public ExprEntity<V> get(K key) { 
            return new PathEntity<V>(type, forMapAccess(this, key));
        }
        public PathMetadata<String> getMetadata() {return metadata;}    
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
    }
    
    public static class PathEntityRenamable<D> extends PathEntity<D>{
        protected PathEntityRenamable(Class<D> type, PathMetadata<?> metadata) {super(type, metadata);}
        public AliasEntity<D> as(PathEntity<D> to) {return Grammar.as(this, to);}
    }
    
    public interface PathMap<K,V> extends Path<Map<K,V>>{
        Expr<V> get(Expr<K> key);
        Expr<V> get(K key);
    }
    
    public interface PathNoEntity<D> extends Path<D>{
        Expr<D> as(String to);              
    }
    
    public static class PathNoEntitySimple<D> extends ExprNoEntity<D> implements PathNoEntity<D>{
        private final PathMetadata<?> metadata;
        public <T> PathNoEntitySimple(Class<D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
    }
    
    public static class PathString extends ExprString implements PathNoEntity<String>{
        private final PathMetadata<String> metadata;
        public PathString(PathMetadata<String> metadata) {
            this.metadata = metadata;
        }
        public PathMetadata<String> getMetadata() {return metadata;}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
    }
        
}
