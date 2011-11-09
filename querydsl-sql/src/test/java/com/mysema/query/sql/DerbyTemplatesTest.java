package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;

public class DerbyTemplatesTest {
    
    @Test
    public void NextVal() {
        Operation<String> nextval = new OperationImpl<String>(String.class, SQLTemplates.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("next value for myseq", new SQLSerializer(new DerbyTemplates()).handle(nextval).toString());        
    }

}
