package com.mysema.query;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.path.PEntity;

import static com.mysema.query.Constants.*;

public abstract class UpdateBaseTest extends AbstractBaseTest{
    
    protected SQLUpdateClause update(PEntity<?> e){
        return new SQLUpdateClause(Connections.getConnection(), dialect, e);
    }
    
    @Test
    public void testUpdate(){        
        // original state
        long count = query().from(survey).count();
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());
        
        // update call with 0 update count
        assertEquals(0, update(survey).where(survey.name.eq("XXX")).set(survey.name, "S").execute());
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());
        
        // update call with full update count
        assertEquals(count, update(survey).set(survey.name, "S").execute());
        assertEquals(count, query().from(survey).where(survey.name.eq("S")).count());
    }
    

}
