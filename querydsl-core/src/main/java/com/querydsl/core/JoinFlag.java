/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;

/**
 * {@code JoinFlag} defines a join related flag using an Expression instance
 *
 * <p>{@code JoinFlag} instances can be used in Querydsl modules which serialize queries to String form.</p>
 *
 * @author tiwe
 *
 */
@Immutable
public class JoinFlag implements Serializable{

    public enum Position {
        
        /**
         * before the join
         */
        START,  
        
        /**
         * as a replacement for the join symbol
         */
        OVERRIDE,
        
        /**
         * before the join target
         */
        BEFORE_TARGET, 

        /**
         * before the join condition
         */
        BEFORE_CONDITION,
        
        /**
         * after the join 
         */
        END
        
    }
    
    private static final long serialVersionUID = -688265393547206465L;
    
    private final Expression<?> flag;
    
    private final Position position;

    /**
     * Create a new instance using the given flag
     * <p>The used position is before the target.</p>
     *
     * @param flag flag in String form
     */
    public JoinFlag(String flag) {
        this(ExpressionUtils.template(Object.class, flag), Position.BEFORE_TARGET);
    }

    /**
     * Create a new instance using the given flag and position.
     *
     * @param flag flag in String form
     * @param position position of the flag in the join
     */
    public JoinFlag(String flag, Position position) {
        this(ExpressionUtils.template(Object.class, flag), position);
    }

    /**
     * Create a new instance using the given flag
     * <p>The used position is before the target.</p>
     *
     * @param flag flag in Expression form
     */
    public JoinFlag(Expression<?> flag) {
        this(flag, Position.BEFORE_TARGET);
    }

    /**
     * Create a new instance using the given flag and position
     *
     * @param flag flag in Expression form
     * @param position position of the flag in the join
     */
    public JoinFlag(Expression<?> flag, Position position) {
        this.flag = flag;
        this.position = position;
    }
    
    @Override
    public int hashCode() {
        return flag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof JoinFlag) {
            return ((JoinFlag)obj).flag.equals(flag);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return flag.toString();
    }

    public Expression<?> getFlag() {
        return flag;
    }

    public Position getPosition() {
        return position;
    }
    
}
