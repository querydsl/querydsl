/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static com.mysema.query.grammar.types.PathMetadata.*;

import java.lang.reflect.Array;

import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.EEntity;
import com.mysema.query.grammar.types.Expr.ESimple;
import com.mysema.query.grammar.types.Expr.EString;


/**
 * Path represents a path expression
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Path<C> {
        
    PathMetadata<?> getMetadata();
    EBoolean isnotnull();
    EBoolean isnull();
            
    public static abstract class PArray<D> extends Expr<D[]> implements Path<D[]>, CollectionType<D>{
        protected final Class<D[]> arrayType;
        protected final Class<D> componentType;
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;        
        private EComparable<Integer> size;
        @SuppressWarnings("unchecked")
        public PArray(Class<D> type, PathMetadata<?> metadata) {
            super(null);
            this.arrayType = (Class<D[]>) Array.newInstance(type, 0).getClass();
            this.componentType = type;
            this.metadata = metadata;            
        }
        public abstract Expr<D> get(Expr<Integer> index);
        public abstract Expr<D> get(int index);
        public Class<D> getElementType() {return componentType;}
        public PathMetadata<?> getMetadata() {return metadata;}
        public Class<D[]> getType(){ return arrayType;}
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }        
        public EComparable<Integer> size() { 
            return size == null ? size = new PComparable<Integer>(Integer.class, forSize(this)) : size;
        }
    }
    
    /**
     * The Class Boolean.
     */
    public static class PBoolean extends EBoolean implements Path<Boolean>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;        
        public PBoolean(PathMetadata<?> metadata) {
            this.metadata = metadata;
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }         
    }
    
    public static class PBooleanArray extends PArray<Boolean>{
        public PBooleanArray(PathMetadata<?> metadata) {
            super(Boolean.class, metadata);
        }
        public EBoolean get(Expr<Integer> index) {
            return new PBoolean(forArrayAccess(this, index));
        }
        public EBoolean get(int index) {
            return new PBoolean(forArrayAccess(this, index));
        }        
    }
    
    /**
     * The Interface Collection.
     */
    public interface PCollection<D> extends Path<java.util.Collection<D>>, CollectionType<D>{        
        Class<D> getElementType();
        EComparable<Integer> size();
    }
    
    /**
     * The Class Comparable.
     */
    public static class PComparable<D extends Comparable<D>> extends EComparable<D> implements Path<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        public PComparable(Class<D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }
    }
        
    public static class PComparableArray<D extends Comparable<D>> extends PArray<D>{
        public PComparableArray(Class<D> type, PathMetadata<?> metadata) {
            super(type, metadata);
        }
        public EComparable<D> get(Expr<Integer> index) {
            return new PComparable<D>(componentType, forArrayAccess(this, index));
        }
        public EComparable<D> get(int index) {
            return new PComparable<D>(componentType, forArrayAccess(this, index));
        }  
    }
    
    /**
     * The Class ComponentCollection.
     */
    public static class PComponentCollection<D> extends ESimple<java.util.Collection<D>> implements PCollection<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private EComparable<Integer> size;
        protected final Class<D> type;
        public PComponentCollection(Class<D> type, PathMetadata<?> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
        }
        public Class<D> getElementType() {return type;}
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }
        public EComparable<Integer> size() { 
            return size == null ? size = new PComparable<Integer>(Integer.class, forSize(this)) : size;
        }
    }
    
    /**
     * The Class ComponentList.
     */
    public static class PComponentList<D> extends PComponentCollection<D> implements PList<D>{        
        public PComponentList(Class<D> type, PathMetadata<?> metadata) {
            super(type, metadata);
        }
        public ESimple<D> get(Expr<Integer> index) {
            return new PSimple<D>(type, forListAccess(this, index));
        }
        public ESimple<D> get(int index) {
            return new PSimple<D>(type, forListAccess(this, index));
        }
    }
    
    /**
     * The Class ComponentMap.
     */
    public static class PComponentMap<K,V> extends ESimple<java.util.Map<K,V>> implements PMap<K,V>{
        private EBoolean isnull, isnotnull;
        private final Class<K> keyType;
        private final PathMetadata<?> metadata;
        private final Class<V> valueType;
        public PComponentMap(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata) {
            super(null);            
            this.keyType = keyType;
            this.valueType = valueType;
            this.metadata = metadata;
        }
        public ESimple<V> get(Expr<K> key) { 
            return new PSimple<V>(valueType, forMapAccess(this, key));
        }
        public ESimple<V> get(K key) { 
            return new PSimple<V>(valueType, forMapAccess(this, key));
        }
        public Class<K> getKeyType() {return keyType; }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Class<V> getValueType() {return valueType; }
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }
    }
    
    /**
     * The Class Entity.
     */
    public static class PEntity<D> extends EEntity<D> implements Path<D>{
        private EBoolean isnull, isnotnull;          
        private final PathMetadata<?> metadata;
        public PEntity(Class<D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public PEntity(Class<D> type, String localName) {
            super(type);
            metadata = forVariable(localName);
        }
        protected PBoolean _boolean(String path){
            return new PBoolean(forProperty(this, path));
        }
        protected <A extends Comparable<A>> PComparable<A> _comparable(String path,Class<A> type) {
            return new PComparable<A>(type, forProperty(this, path));
        }        
        protected <A> PEntity<A> _entity(String path, Class<A> type){
            return new PEntity<A>(type, forProperty(this, path)); 
        }
        protected <A> PEntityCollection<A> _entitycol(String path,Class<A> type) {
            return new PEntityCollection<A>(type, forProperty(this, path));
        }
        protected <A> PEntityList<A> _entitylist(String path,Class<A> type) {
            return new PEntityList<A>(type, forProperty(this, path));
        }
        protected <A> PSimple<A> _simple(String path, Class<A> type){
            return new PSimple<A>(type, forProperty(this, path));
        }        
        protected <A> PComponentCollection<A> _simplecol(String path,Class<A> type) {
            return new PComponentCollection<A>(type, forProperty(this, path));
        }  
        protected <A> PComponentList<A> _simplelist(String path,Class<A> type) {
            return new PComponentList<A>(type, forProperty(this, path));
        }
        protected <K,V> PComponentMap<K,V> _simplemap(String path, Class<K> key, Class<V> value){
            return new PComponentMap<K,V>(key, value, forProperty(this, path));
        }
        protected PString _string(String path){
            return new PString(forProperty(this, path));
        }
        public Alias.AEntity<D> as(PEntity<D> to) {return IntGrammar.as(this, to);}
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean in(CollectionType<D> right){return IntGrammar.in(this, right);}
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }
        public <B extends D> EBoolean typeOf(Class<B> type) {return IntGrammar.typeOf(this, type);}
    }
    
    /**
     * The Class EntityCollection.
     */
    public static class PEntityCollection<D> extends EEntity<java.util.Collection<D>> implements PCollection<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private EComparable<Integer> size;        
        protected final Class<D> type;

        public PEntityCollection(Class<D> type, PathMetadata<?> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
        }
        public Alias.AEntityCollection<D> as(PEntity<D> to) {return IntGrammar.as(this, to);}
        public Class<D> getElementType() {return type;}
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }
        public EComparable<Integer> size() { 
            return size == null ? size = new PComparable<Integer>(Integer.class, forSize(this)) : size;
        }
    }
    
    /**
     * The Class EntityList.
     */
    public static class PEntityList<D> extends PEntityCollection<D> implements PList<D>{
        public PEntityList(Class<D> type, PathMetadata<?> metadata) {
            super(type, metadata);
        }
        public EEntity<D> get(Expr<Integer> index) {
            return new PEntity<D>(type, forListAccess(this,index));
        }
        public EEntity<D> get(int index) {
            return new PEntity<D>(type, forListAccess(this,index));
        }
    }
    
    /**
     * The Class EntityMap.
     */
    public static class PEntityMap<K,V> extends EEntity<PMap<K,V>> implements PMap<K,V>{
        private EBoolean isnull, isnotnull;
        private final Class<K> keyType;
        private final PathMetadata<?> metadata;
        private final Class<V> valueType; 
        public PEntityMap(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata) {
            super(null);            
            this.keyType = keyType;
            this.valueType = valueType;
            this.metadata = metadata;
        }
        public EEntity<V> get(Expr<K> key) { 
            return new PEntity<V>(valueType, forMapAccess(this, key));
        }
        public EEntity<V> get(K key) { 
            return new PEntity<V>(valueType, forMapAccess(this, key));
        }    
        public Class<K> getKeyType() {return keyType; }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Class<V> getValueType() {return valueType; }
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }
    }
    
    /**
     * The Interface List.
     */
    public interface PList<D> extends PCollection<D>{
        Expr<D> get(Expr<Integer> index);        
        Expr<D> get(int index);
    }
    
    /**
     * The Interface Map.
     */
    public interface PMap<K,V> extends Path<java.util.Map<K,V>>{
        Expr<V> get(Expr<K> key);
        Expr<V> get(K key);
        Class<K> getKeyType();
        Class<V> getValueType();
    }
    
    /**
     * The Class Simple.
     */
    public static class PSimple<D> extends ESimple<D> implements Path<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        public PSimple(Class<D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }
    }
    
//    /**
//     * The Class RenamableEntity.
//     */
//    public static class RenamableEntity<D> extends Entity<D>{
//        protected RenamableEntity(Class<D> type, PathMetadata<?> metadata) {super(type, metadata);}
//        public Alias.Entity<D> as(Entity<D> to) {return IntGrammar.as(this, to);}
//    }
        
    /**
     * The Class String.
     */
    public static class PString extends EString implements Path<String>{
        private EBoolean isnull, isnotnull;        
        private final PathMetadata<?> metadata;
        public PString(PathMetadata<?> metadata) {
            this.metadata = metadata;
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            return isnotnull == null ? isnotnull = IntGrammar.isnotnull(this) : isnotnull; 
        }
        public EBoolean isnull() {
            return isnull == null ? isnull = IntGrammar.isnull(this) : isnull; 
        }
    }
    
    public static class PStringArray extends PArray<String>{
        public PStringArray(PathMetadata<?> metadata) {
            super(String.class, metadata);
        }
        public EString get(Expr<Integer> index) {
            return new PString(forArrayAccess(this, index));
        }
        public EString get(int index) {
            return new PString(forArrayAccess(this, index));
        }      
    }

}
