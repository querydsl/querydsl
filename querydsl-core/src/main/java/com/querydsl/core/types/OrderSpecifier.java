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
package com.querydsl.core.types;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * OrderSpecifier represents an order-by-element in a Query instance
 *
 * @param <T> related expression type
 * @author tiwe
 */
@Immutable
public class OrderSpecifier<T extends Comparable> implements Serializable {

    private static final long serialVersionUID = 3427652988262514678L;

    public enum NullHandling { Default, NullsFirst, NullsLast }
    
    private final Order order;

    private final Expression<T> target;
    
    private final NullHandling nullHandling;

    public OrderSpecifier(Order order, Expression<T> target, NullHandling nullhandling) {
        this.order = order;
        this.target = target;
        this.nullHandling = nullhandling;
    }
    
    public OrderSpecifier(Order order, Expression<T> target) {
        this(order, target, NullHandling.Default);        
    }

    /**
     * Get the order of this specifier
     *
     * @return
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Get whether the order is ascending or not
     *
     * @return
     */
    public boolean isAscending() {
        return order == Order.ASC;
    }

    /**
     * Get the target expression of this OrderSpecifier
     *
     * @return
     */
    public Expression<T> getTarget() {
        return target;
    }

    /**
     * @return
     */
    public NullHandling getNullHandling() {
        return nullHandling;
    }
    
    /**
     * @return
     */
    public OrderSpecifier<T> nullsFirst() {
        return new OrderSpecifier<T>(order, target, NullHandling.NullsFirst);
    }
    
    /**
     * @return
     */
    public OrderSpecifier<T> nullsLast() {
        return new OrderSpecifier<T>(order, target, NullHandling.NullsLast);
    }

    @Override
    public String toString() {
        return target + " " + order;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof OrderSpecifier) {
            OrderSpecifier<?> os = (OrderSpecifier)o;
            return os.order.equals(order) && os.target.equals(target)
                    && os.nullHandling.equals(nullHandling);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

}
