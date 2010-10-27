package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Templates;
import com.mysema.query.types.ToStringVisitor;
import com.mysema.query.types.path.StringPath;

public class OperationTest {
    
    enum ExampleEnum {A,B}

    @SuppressWarnings("unchecked")
    @Test
    public void Various(){
        Expression[] args = new Expression[]{new StringPath("x"), new StringPath("y")};
        List<Operation<?>> operations = new ArrayList<Operation<?>>();
//        paths.add(new ArrayOperation(String[].class, "p"));
        operations.add(new BooleanOperation(Ops.EQ_OBJECT, args));
        operations.add(new ComparableOperation(String.class, Ops.SUBSTR_1ARG, args));
        operations.add(new DateOperation(Date.class, Ops.DateTimeOps.CURRENT_DATE, args));
        operations.add(new DateTimeOperation(Date.class,Ops.DateTimeOps.CURRENT_TIMESTAMP, args));
        operations.add(new EnumOperation(ExampleEnum.class,Ops.IS_NOT_NULL, args));
        operations.add(new NumberOperation(Integer.class,Ops.ADD, args));
        operations.add(new SimpleOperation(String.class,Ops.EQ_OBJECT, args));
        operations.add(new StringOperation(Ops.CONCAT, args));
        operations.add(new TimeOperation(Time.class,Ops.DateTimeOps.CURRENT_TIME, args));
        
        for (Operation<?> operation : operations){
            Operation<?> other = new OperationImpl(operation.getType(), operation.getOperator(), operation.getArgs());
            assertEquals(operation.toString(), operation.accept(ToStringVisitor.DEFAULT, Templates.DEFAULT));
            assertEquals(operation.hashCode(), other.hashCode());
            assertEquals(operation, other);
            assertNotNull(operation.getOperator());
            assertNotNull(operation.getArgs());
            assertNotNull(operation.getType());            
        }
    }
    
}
