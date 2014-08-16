/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysema.query.mongodb.aggregation;

import com.mysema.query.types.Path;

/**
 *  List of aggregation stages that should be performed by an aggregation Operation
 * @author Komi Innocent 
 */
public class Aggregation {

    /**
     * Create an projection operation for the given fields
     * @param <T>
     * @param fields
     * @return 
     */
    public static <T> ProjectOperation project(Path<T>... fields) {
        return new ProjectOperation((fields));
    }
    
    /**
     * 
     * @param <T>
     * @return 
     */
    public static <T> ProjectOperation project() {
        return new ProjectOperation(null);
    }
}
