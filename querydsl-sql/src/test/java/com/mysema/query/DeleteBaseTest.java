/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.Target.MYSQL;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.types.path.PEntity;
import com.mysema.testutil.ExcludeIn;

import static com.mysema.query.Constants.*;

public abstract class DeleteBaseTest extends AbstractBaseTest{
    
    protected SQLDeleteClause delete(PEntity<?> e){
        return new SQLDeleteClause(Connections.getConnection(), dialect, e);
    }
    
    @Test
    @ExcludeIn(MYSQL)
    public void delete() throws SQLException{
        try{
            // TODO : FIXME
            long count = query().from(survey).count();
            assertEquals(0, delete(survey).where(survey.name.eq("XXX")).execute());
            assertEquals(count, delete(survey).execute());    
        }finally{
            Connections.getStatement().execute("insert into survey values (1, 'Hello World')");    
        }
    }

}
