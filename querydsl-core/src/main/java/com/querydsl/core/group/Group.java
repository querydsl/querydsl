/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.group;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.querydsl.core.types.Expression;

/**
 * A group of rows. Group is build according to GroupDefinitions. 
 * 
 * @author sasa
 *
 */
public interface Group {
    
    /**
     * @return Groups elements as an array
     */
    Object[] toArray();
    
    /**
     * Returns the value of the given group. 
     * 
     * @param <T> Type of element in a single ResultSet row, i.e. type of {@code Expression<T>}
     * @param <R> Target type of this group, e.g. {@code List<T>}
     * @param coldef 
     * @throws NoSuchElementException if group is undefined.
     * @throws ClassCastException if group is of different type  
     * @return Value of given group definition in this group
     */
    <T, R> R getGroup(GroupExpression<T, R> coldef);
    
    /**
     * Returns the value of the given single valued expression. This is the 
     * first value of given column within this group of the ResultSet. 
     * 
     * @param <T> Value type
     * @param expr Grouped expression
     * @throws NoSuchElementException if group is undefined.  
     * @throws ClassCastException if group is of different type (e.g. Set)
     * @return Value of given expression in this group
     */
    <T> T getOne(Expression<T> expr);
    
    /**
     * Returns a Set of values in this group. 
     * 
     * @param <T> Value type of Set
     * @param expr Grouped expression
     * @throws NoSuchElementException if group is undefined.  
     * @throws ClassCastException if group is of different type (e.g. List)
     * @return Set of values in this group
     */
    <T> Set<T> getSet(Expression<T> expr);
    
    /**
     * Returns a List of values in this group. 
     * 
     * @param <T> Value type of List
     * @param expr Grouped expression
     * @throws NoSuchElementException if group is undefined.  
     * @throws ClassCastException if group is of different type (e.g. Set)
     * @return List of values in this group
     */
    <T> List<T> getList(Expression<T> expr);
    
    /**
     * Returns a Map of values in this group
     * 
     * @param <K> Key type of result Map
     * @param <V> Value type of result Map
     * @param key Key expression
     * @param value Value expression
     * @throws NoSuchElementException if group is undefined.  
     * @throws ClassCastException if group is of different type (e.g. List)
     * @return Map of values in this group
     */
    <K, V> Map<K, V> getMap(Expression<K> key, Expression<V> value);
    
}