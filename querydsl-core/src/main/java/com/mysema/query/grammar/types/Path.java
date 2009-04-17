/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static com.mysema.query.grammar.types.PathMetadata.forArrayAccess;
import static com.mysema.query.grammar.types.PathMetadata.forListAccess;
import static com.mysema.query.grammar.types.PathMetadata.forMapAccess;
import static com.mysema.query.grammar.types.PathMetadata.forProperty;
import static com.mysema.query.grammar.types.PathMetadata.forSize;

import java.lang.reflect.Array;
import java.util.Map;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.types.Expr.*;


/**
 * Path represents a path expression
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Path<C> {
        
    PathMetadata<?> getMetadata();
    Path<?> getRoot();
    EBoolean isnotnull();
    EBoolean isnull();
    Class<? extends C> getType();
                
    public static abstract class PArray<D> extends Expr<D[]> implements Path<D[]>, CollectionType<D>{
        protected final Class<D[]> arrayType;
        protected final Class<D> componentType;
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private EComparable<Integer> size;
        private final Path<?> root;
        @SuppressWarnings("unchecked")
        public PArray(Class<D> type, PathMetadata<?> metadata) {
            super(null);
            this.arrayType = (Class<D[]>) Array.newInstance(type, 0).getClass();
            this.componentType = type;
            this.metadata = metadata;    
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PArray(Class<D> type, String var){
            this(type, PathMetadata.forVariable(var));
        }        
        
        public abstract Expr<D> get(Expr<Integer> index);
        public abstract Expr<D> get(int index);
        public Class<D> getElementType() {return componentType;}
        public PathMetadata<?> getMetadata() {return metadata;}
        public Class<D[]> getType(){ return arrayType;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }        
        public EComparable<Integer> size() {
            if (size == null) size = new PComparable<Integer>(Integer.class, forSize(this));
            return size;
        }
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }

    public static class PBoolean extends EBoolean implements Path<Boolean>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private final Path<?> root;
        public PBoolean(PathMetadata<?> metadata) {
            this.metadata = metadata;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PBoolean(String var){
            this(PathMetadata.forVariable(var));
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public Path<?> getRoot(){
            return root;
        }    
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }
    
    public static class PBooleanArray extends PArray<Boolean>{
        public PBooleanArray(PathMetadata<?> metadata) {
            super(Boolean.class, metadata);
        }
        public PBooleanArray(String var){
            super(Boolean.class, PathMetadata.forVariable(var));
        }
        public EBoolean get(Expr<Integer> index) {
            return new PBoolean(forArrayAccess(this, index));
        }
        public EBoolean get(int index) {
            return new PBoolean(forArrayAccess(this, index));
        }        
        
    }

    public interface PCollection<D> extends Path<java.util.Collection<D>>, CollectionType<D>{        
        Class<D> getElementType();
        EBoolean contains(D child);
        EBoolean contains(Expr<D> child);
        EComparable<Integer> size();
    }

    public static class PComparable<D extends Comparable<? super D>> extends EComparable<D> implements Path<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private final Path<?> root;
        public PComparable(Class<? extends D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PComparable(Class<? extends D> type, String var) {
            this(type, PathMetadata.forVariable(var));
        }        
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }
    
    public static class PNumber<D extends Number & Comparable<? super D>> extends ENumber<D> implements Path<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private final Path<?> root;
        public PNumber(Class<? extends D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PNumber(Class<? extends D> type, String var){
            this(type, PathMetadata.forVariable(var));
        }        
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }
        
    public static class PComparableArray<D extends Comparable<? super D>> extends PArray<D>{
        public PComparableArray(Class<D> type, PathMetadata<?> metadata) {
            super(type, metadata);
        }
        public PComparableArray(Class<D> type, String var){
            super(type, PathMetadata.forVariable(var));
        }
        public EComparable<D> get(Expr<Integer> index) {
            return new PComparable<D>(componentType, forArrayAccess(this, index));
        }
        public EComparable<D> get(int index) {
            return new PComparable<D>(componentType, forArrayAccess(this, index));
        }  
    }

    public static class PComponentCollection<D> extends ESimple<java.util.Collection<D>> implements PCollection<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private EComparable<Integer> size;
        protected final Class<D> type;
        private final Path<?> root;
        public PComponentCollection(Class<D> type, PathMetadata<?> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PComponentCollection(Class<D> type, String var){
            this(type, PathMetadata.forVariable(var));   
        }        
        public Class<D> getElementType() {return type;}
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public EComparable<Integer> size() {
            if (size == null) size = new PComparable<Integer>(Integer.class, forSize(this));
            return size;
        }
        public EBoolean contains(D child) {
            return Grammar.in(child, this);
        }
        public EBoolean contains(Expr<D> child) {
            return Grammar.in(child, this);
        }
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }
 
    public static class PComponentList<D> extends PComponentCollection<D> implements PList<D>{        
        public PComponentList(Class<D> type, PathMetadata<?> metadata) {
            super(type, metadata);
        }
        public PComponentList(Class<D> type, String var){
            super(type, PathMetadata.forVariable(var));
        }        
        public ESimple<D> get(Expr<Integer> index) {
            return new PSimple<D>(type, forListAccess(this, index));
        }
        public ESimple<D> get(int index) {
            return new PSimple<D>(type, forListAccess(this, index));
        }        
    }

    public static class PComponentMap<K,V> extends ESimple<java.util.Map<K,V>> implements PMap<K,V>{
        private EBoolean isnull, isnotnull;
        private final Class<K> keyType;
        private final PathMetadata<?> metadata;
        private final Class<V> valueType;
        private final Path<?> root;
        public PComponentMap(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata) {
            super(null);            
            this.keyType = keyType;
            this.valueType = valueType;
            this.metadata = metadata;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PComponentMap(Class<K> keyType, Class<V> valueType, String var){
            this(keyType, valueType, PathMetadata.forVariable(var));
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
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }

    public static class PEntity<D> extends EEntity<D> implements Path<D>{
        private EBoolean isnull, isnotnull;          
        private final PathMetadata<?> metadata;
        private final String entityName;
        private final Path<?> root;
        public PEntity(Class<? extends D> type, String entityName, PathMetadata<?> metadata) {
            super(type);
            this.entityName = entityName;
            this.metadata = metadata;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PEntity(Class<? extends D> type, String entityName, String var) {
            this(type, entityName, PathMetadata.forVariable(var));
        }
        protected PBoolean _boolean(String path){
            return new PBoolean(forProperty(this, path));
        }
        protected <A extends Comparable<? super A>> PComparable<A> _comparable(String path,Class<A> type) {
            return new PComparable<A>(type, forProperty(this, path));
        }    
        protected <A extends Number & Comparable<? super A>> PNumber<A> _number(String path,Class<A> type) {
            return new PNumber<A>(type, forProperty(this, path));
        }   
        protected <A> PEntity<A> _entity(String path, String entityName, Class<A> type){
            return new PEntity<A>(type, entityName, forProperty(this, path)); 
        }
        protected <A> PEntityCollection<A> _entitycol(String path, Class<A> type, String entityName) {
            return new PEntityCollection<A>(type, entityName, forProperty(this, path));
        }
        protected <A> PEntityList<A> _entitylist(String path, Class<A> type, String entityName) {
            return new PEntityList<A>(type, entityName, forProperty(this,  path));
        }
        protected <K,V> PEntityMap<K,V> _entitymap(String path, Class<K> key, Class<V> value, String entityName){
            return new PEntityMap<K,V>(key, value, entityName, forProperty(this, path));
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
        public Alias.AEntity<D> as(PEntity<D> to) {return Grammar.as(this, to);}
        public String getEntityName(){ return entityName; }
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public <B extends D> EBoolean typeOf(Class<B> type) {return Grammar.typeOf(this, type);}
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }

    public static class PEntityCollection<D> extends EEntity<java.util.Collection<D>> implements PCollection<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private ENumber<Integer> size;        
        protected final Class<D> type;
        protected final String entityName;
        private final Path<?> root;
        public PEntityCollection(Class<D> type, String entityName, PathMetadata<?> metadata) {
            super(null);            
            this.type = type;
            this.metadata = metadata;
            this.entityName = entityName;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PEntityCollection(Class<D> type, String entityName, String var){
            this(type, entityName, PathMetadata.forVariable(var));
        }        
        public Alias.AEntityCollection<D> as(PEntity<D> to) {return Grammar.as(this, to);}
        public Class<D> getElementType() {return type;}
        public String getEntityName() { return entityName; }
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public ENumber<Integer> size() { 
            if (size == null) size = new PNumber<Integer>(Integer.class, forSize(this));
            return size;
        }
        public EBoolean contains(D child) {
            return Grammar.in(child, this);
        }
        public EBoolean contains(Expr<D> child) {
            return Grammar.in(child, this);
        }
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }

    public static class PEntityList<D> extends PEntityCollection<D> implements PList<D>{
        public PEntityList(Class<D> type, String entityName, PathMetadata<?> metadata) {
            super(type, entityName, metadata);
        }
        public PEntityList(Class<D> type, String entityName, String var){
            super(type, entityName, PathMetadata.forVariable(var));
        }        
        public PEntity<D> get(Expr<Integer> index) {
            return new PEntity<D>(type, entityName, forListAccess(this,index));
        }
        public PEntity<D> get(int index) {
            // cache
            return new PEntity<D>(type, entityName, forListAccess(this,index));
        }
        
    }

    public static class PEntityMap<K,V> extends EEntity<Map<K,V>> implements PMap<K,V>{
        private EBoolean isnull, isnotnull;
        private final Class<K> keyType;
        private final PathMetadata<?> metadata;
        private final Class<V> valueType; 
        private final String entityName;
        private final Path<?> root;
        public PEntityMap(Class<K> keyType, Class<V> valueType, String entityName, PathMetadata<?> metadata) {
            super(null);            
            this.keyType = keyType;
            this.valueType = valueType;
            this.entityName = entityName;
            this.metadata = metadata;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PEntityMap(Class<K> keyType, Class<V> valueType, String entityName, String var){
            this(keyType, valueType, entityName, PathMetadata.forVariable(var));
        }
        
        public PEntity<V> get(Expr<K> key) { 
            return new PEntity<V>(valueType, entityName, forMapAccess(this, key));
        }
        public PEntity<V> get(K key) { 
            return new PEntity<V>(valueType, entityName, forMapAccess(this, key));
        }    
        public Class<K> getKeyType() {return keyType; }
        public PathMetadata<?> getMetadata() {return metadata;}
        public Class<V> getValueType() {return valueType; }
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }

    public interface PList<D> extends PCollection<D>{
        Expr<D> get(Expr<Integer> index);        
        Expr<D> get(int index);
    }

    public interface PMap<K,V> extends Path<java.util.Map<K,V>>{
        Expr<V> get(Expr<K> key);
        Expr<V> get(K key);
        Class<K> getKeyType();
        Class<V> getValueType();
    }

    public static class PSimple<D> extends ESimple<D> implements Path<D>{
        private EBoolean isnull, isnotnull;
        private final PathMetadata<?> metadata;
        private final Path<?> root;
        public PSimple(Class<? extends D> type, PathMetadata<?> metadata) {
            super(type);
            this.metadata = metadata;
            this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public PSimple(Class<? extends D> type, String var){
            this(type, PathMetadata.forVariable(var));
        }        
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public Path<?> getRoot(){
            return root;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }    

    public static class PString extends EString implements Path<String>{
        private EBoolean isnull, isnotnull;        
        private final PathMetadata<?> metadata;
        public PString(PathMetadata<?> metadata) {
            this.metadata = metadata;
        }
        public PString(String var){
            this(PathMetadata.forVariable(var));
        }
        public PathMetadata<?> getMetadata() {return metadata;}
        public EBoolean isnotnull() {
            if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
            return isnotnull; 
        }
        public EBoolean isnull() {
            if (isnull == null) isnull = Grammar.isnull(this);
            return isnull; 
        }     
        public Path<?> getRoot(){
            return metadata.getRoot() != null ? metadata.getRoot() : this;
        }
        public int hashCode(){
            return metadata.hashCode();
        }        
        public boolean equals(Object o){
            return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
        }
    }
    
    public static class PStringArray extends PArray<String>{
        public PStringArray(PathMetadata<?> metadata) {
            super(String.class, metadata);
        }
        public PStringArray(String var){
            super(String.class, PathMetadata.forVariable(var));
        }
        public EString get(Expr<Integer> index) {
            return new PString(forArrayAccess(this, index));
        }
        public EString get(int index) {
            // TODO : cache
            return new PString(forArrayAccess(this, index));
        }             
    }

}
