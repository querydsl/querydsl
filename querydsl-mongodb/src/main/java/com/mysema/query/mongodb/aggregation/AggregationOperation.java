/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysema.query.mongodb.aggregation;

import com.mongodb.DBObject;
import com.mysema.query.mongodb.MongodbSerializer;

/**
 * Represents an operation in the aggregation pipeline
 *
 * @author Komi Innocent
 */
public interface AggregationOperation<T> {

    DBObject toDBObject(MongodbSerializer serializer);
}
