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
package com.querydsl.core.types.expr;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Templates;
import com.querydsl.core.types.ToStringVisitor;
import com.querydsl.core.types.path.StringPath;

public class OperationTest {
    
    enum ExampleEnum {A,B}

    @SuppressWarnings("unchecked")
    @Test
    public void Various() {
        Expression[] args = new Expression[]{new StringPath("x"), new StringPath("y")};
        List<Operation<?>> operations = new ArrayList<Operation<?>>();
//        paths.add(new ArrayOperation(String[].class, "p"));
        operations.add(new BooleanOperation(Ops.EQ, args));
        operations.add(new ComparableOperation(String.class, Ops.SUBSTR_1ARG, args));
        operations.add(new DateOperation(Date.class, Ops.DateTimeOps.CURRENT_DATE, args));
        operations.add(new DateTimeOperation(Date.class,Ops.DateTimeOps.CURRENT_TIMESTAMP, args));
        operations.add(new EnumOperation(ExampleEnum.class,Ops.IS_NOT_NULL, args));
        operations.add(new NumberOperation(Integer.class,Ops.ADD, args));
        operations.add(new SimpleOperation(String.class,Ops.EQ, args));
        operations.add(new StringOperation(Ops.CONCAT, args));
        operations.add(new TimeOperation(Time.class,Ops.DateTimeOps.CURRENT_TIME, args));
        
        for (Operation<?> operation : operations) {
            Operation<?> other = new OperationImpl(operation.getType(), operation.getOperator(), 
                    ImmutableList.copyOf(operation.getArgs()));
            assertEquals(operation.toString(), operation.accept(ToStringVisitor.DEFAULT, Templates.DEFAULT));
            assertEquals(operation.hashCode(), other.hashCode());
            assertEquals(operation, other);
            assertNotNull(operation.getOperator());
            assertNotNull(operation.getArgs());
            assertNotNull(operation.getType());            
        }
    }
    
}
