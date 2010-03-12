/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.mssql;

import static com.mysema.query.Constants.employee;
import static com.mysema.query.sql.mssql.SQLServerGrammar.rn;
import static com.mysema.query.sql.mssql.SQLServerGrammar.rowNumber;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.SQLServerTemplates;
import com.mysema.query.sql.mssql.RowNumber;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.SubQuery;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@RunWith(FilteringTestRunner.class)
@ResourceCheck("/sqlserver.run")
@Label(Target.SQLSERVER)
public class SelectMSSQLTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initSQLServer();
    }

    @Before
    public void setUpForTest() {
        dialect = new SQLServerTemplates().newLineToSingleSpace();
    }
    
    @Test
    public void testPaging(){
        RowNumber rowNumber = rowNumber().orderBy(employee.lastname.asc()).as(rn);
        // TODO : create a short cut for wild card
        Expr<Object[]> all = CSimple.create(Object[].class, "*");
        
        // simple
        for (Object[] row : query().from(employee).list(employee.firstname, employee.lastname, rowNumber)){
            System.out.println(Arrays.asList(row));
        }
        
        // with subquery
        ListSubQuery<Object[]> sub = s().from(employee).list(employee.firstname, employee.lastname, rowNumber);
        PSimple<Object[]> subAlias = new PSimple<Object[]>(Object[].class, "s");
        for (Object[] row : query().from(sub, subAlias).list(all)){
            System.out.println(Arrays.asList(row));
        }
        
        // with subquery, only row number
        SubQuery<Long> sub2 = s().from(employee).unique(rowNumber);
        PSimple<Long> subAlias2 = new PSimple<Long>(Long.class, "s");
        for (Object[] row : query().from(sub2, subAlias2).list(all)){
            System.out.println(Arrays.asList(row));
        }
    }
    
}
