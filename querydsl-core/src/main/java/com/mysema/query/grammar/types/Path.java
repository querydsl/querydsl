/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static com.mysema.query.grammar.types.PathMetadata.forListAccess;
import static com.mysema.query.grammar.types.PathMetadata.forMapAccess;
import static com.mysema.query.grammar.types.PathMetadata.forProperty;
import static com.mysema.query.grammar.types.PathMetadata.forSize;
import static com.mysema.query.grammar.types.PathMetadata.forVariable;


/**
 * Path provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Path<C> {
    
    PathMetadata<?> getMetadata();
    Expr.Boolean isnotnull();
    Expr.Boolean isnull();
            
    public static class Boolean extends Expr.Boolean implements Simple<java.lang.Boolean>{
        private final PathMetadata<java.lang.String> metadata;
        public Boolean(PathMetadata<java.lang.String> metadata) {
            this.metadata = metadata;
        }        
        public PathMetadata<java.lang.String> getMetadata() {return metadata;}
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}          
    }
        
    public interface Collection<D> extends Path<java.util.Collection<D>>, Expr.CollectionType<D>{
        Expr<D> get(Expr<Integer> index);        
        Expr<D> get(int index);
        Class<D> getElementType();
        Expr.Comparable<Integer> size();
    }
    
    public static class Comparable<D extends java.lang.Comparable<D>> extends Expr.Comparable<D> implements Simple<D>{
        private final PathMetadata<?> metadata;
        public Comparable(Class<D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}
    }
    
    public static class ComponentCollection<D> extends Expr.Simple<java.util.Collection<D>> implements Collection<D>{
        private final PathMetadata<?> metadata;
        private final Class<D> type;
        public ComponentCollection(Class<D> type, PathMetadata<?> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
        }        
        public Expr.Literal<D> get(Expr<Integer> index) {
            return new Path.Literal<D>(type, forListAccess(this, index));
        }
        public Expr.Literal<D> get(int index) {
            return new Path.Literal<D>(type, forListAccess(this, index));
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}
        public Expr.Comparable<Integer> size() { 
//            return IntGrammar.size(this);
            return new Path.Comparable<Integer>(Integer.class, forSize(this));
        }
        public Class<D> getElementType() {return type;}
    }
    
    public static class ComponentMap<K,V> extends Expr.Simple<java.util.Map<K,V>> implements Map<K,V>{
        private final PathMetadata<?> metadata;
        private final Class<K> keyType;
        private final Class<V> valueType;
        public ComponentMap(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata) {
            super(null);            
            this.keyType = keyType;
            this.valueType = valueType;
            this.metadata = metadata;
        }
        public Expr.Literal<V> get(Expr<K> key) { 
            return new Path.Literal<V>(valueType, forMapAccess(this, key));
        }
        public Expr.Literal<V> get(K key) { 
            return new Path.Literal<V>(valueType, forMapAccess(this, key));
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}
        public Class<K> getKeyType() {return keyType; }
        public Class<V> getValueType() {return valueType; }
    }
    
    public static class Entity<D> extends Expr.Entity<D> implements Path<D>{
        private final PathMetadata<?> metadata;        
        public Entity(Class<D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public Entity(Class<D> type, java.lang.String localName) {
            super(type);
            metadata = forVariable(localName);
        }
        protected Path.Boolean _boolean(java.lang.String path){
            return new Path.Boolean(forProperty(this, path));
        }
        protected <A extends java.lang.Comparable<A>> Path.Comparable<A> _comparable(java.lang.String path,Class<A> type) {
            return new Path.Comparable<A>(type, forProperty(this, path));
        }
        protected <A> RenamableEntity<A> _entity(java.lang.String path, Class<A> type){
            return new RenamableEntity<A>(type, forProperty(this, path)); 
        }        
        protected <A> EntityCollection<A> _entitycol(java.lang.String path,Class<A> type) {
            return new EntityCollection<A>(type, forProperty(this, path));
        }
        protected <A> Expr.Literal<A> _simple(java.lang.String path, Class<A> type){
            return new Path.Literal<A>(type, forProperty(this, path));
        }
        protected <A> ComponentCollection<A> _simplecol(java.lang.String path,Class<A> type) {
            return new ComponentCollection<A>(type, forProperty(this, path));
        }        
        protected <K,V> ComponentMap<K,V> _simplemap(java.lang.String path, Class<K> key, Class<V> value){
            return new ComponentMap<K,V>(key, value, forProperty(this, path));
        }
        protected Path.String _string(java.lang.String path){
            return new Path.String(forProperty(this, path));
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Expr.Boolean in(CollectionType<D> right){return IntGrammar.in(this, right);}
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}
        public <B extends D> Expr.Boolean typeOf(Class<B> type) {return IntGrammar.typeOf(this, type);}
    }
    
    public static class EntityCollection<D> extends Expr.Entity<java.util.Collection<D>> implements Collection<D>{
        private final PathMetadata<?> metadata;
        private final Class<D> type;
        public EntityCollection(Class<D> type, PathMetadata<?> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
        }        
        public Alias.EntityCollection<D> as(Path.Entity<D> to) {return IntGrammar.as(this, to);}
        public Expr.Entity<D> get(Expr<Integer> index) {
            return new Path.Entity<D>(type, forListAccess(this,index));
        }
        public Expr.Entity<D> get(int index) {
            return new Path.Entity<D>(type, forListAccess(this,index));
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}    
        public Expr.Comparable<Integer> size() { 
            return new Path.Comparable<Integer>(Integer.class, forSize(this));
        }
        public Class<D> getElementType() {return type;}
    }
    
    public static class EntityMap<K,V> extends Expr.Entity<Map<K,V>> implements Map<K,V>{
        private final PathMetadata<?> metadata;
        private final Class<K> keyType;
        private final Class<V> valueType;
        public EntityMap(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata) {
            super(null);            
            this.keyType = keyType;
            this.valueType = valueType;
            this.metadata = metadata;
        } 
        public Expr.Entity<V> get(Expr<K> key) { 
            return new Path.Entity<V>(valueType, forMapAccess(this, key));
        }
        public Expr.Entity<V> get(K key) { 
            return new Path.Entity<V>(valueType, forMapAccess(this, key));
        }
        public PathMetadata<?> getMetadata() {return metadata;}    
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}
        public Class<K> getKeyType() {return keyType; }
        public Class<V> getValueType() {return valueType; }
    }
    
    public static class RenamableEntity<D> extends Entity<D>{
        protected RenamableEntity(Class<D> type, PathMetadata<?> metadata) {super(type, metadata);}
        public Alias.Entity<D> as(Path.Entity<D> to) {return IntGrammar.as(this, to);}
    }
    
    public interface Map<K,V> extends Path<java.util.Map<K,V>>{
        Expr<V> get(Expr<K> key);
        Expr<V> get(K key);
        Class<K> getKeyType();
        Class<V> getValueType();
    }
    
    public interface Simple<D> extends Path<D>{
        Expr<D> as(java.lang.String to);              
    }
    
    public static class Literal<D> extends Expr.Literal<D> implements Simple<D>{
        private final PathMetadata<?> metadata;
        public <T> Literal(Class<D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}
    }
    
    public static class String extends Expr.String implements Simple<java.lang.String>{
        private final PathMetadata<java.lang.String> metadata;
        public String(PathMetadata<java.lang.String> metadata) {
            this.metadata = metadata;
        }
        public PathMetadata<java.lang.String> getMetadata() {return metadata;}
        public Expr.Boolean isnotnull() {return IntGrammar.isnotnull(this);}
        public Expr.Boolean isnull() {return IntGrammar.isnull(this);}
    }

}
