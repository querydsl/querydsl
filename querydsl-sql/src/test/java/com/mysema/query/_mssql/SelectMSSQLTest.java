/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query._mssql;

import static com.mysema.query.Constants.employee;
import static com.mysema.query.Constants.employee2;
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
import com.mysema.query.types.query.ObjectSubQuery;
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
    public void manualPaging(){
        RowNumber rowNumber = rowNumber().orderBy(employee.lastname.asc()).as(rn);
        // TODO : create a short cut for wild card
        Expr<Object[]> all = CSimple.create(Object[].class, "*");
        
        // simple
        System.out.println("#1");
        for (Object[] row : query().from(employee).list(employee.firstname, employee.lastname, rowNumber)){
            System.out.println(Arrays.asList(row));
        }
        System.out.println();
        
        // with subquery, generic alias
        System.out.println("#2");
        ListSubQuery<Object[]> sub = s().from(employee).list(employee.firstname, employee.lastname, rowNumber);
        PSimple<Object[]> subAlias = new PSimple<Object[]>(Object[].class, "s");
        for (Object[] row : query().from(sub.as(subAlias)).list(all)){
            System.out.println(Arrays.asList(row));
        }
        System.out.println();
                
        // with subquery, only row number
        System.out.println("#3");
        ObjectSubQuery<Long> sub2 = s().from(employee).unique(rowNumber);
        PSimple<Long> subAlias2 = new PSimple<Long>(Long.class, "s");
        for (Object[] row : query().from(sub2.as(subAlias2)).list(all)){
            System.out.println(Arrays.asList(row));
        }
        System.out.println();
        
        // with subquery, specific alias
        System.out.println("#4");
        ListSubQuery<Object[]> sub3 = s().from(employee).list(employee.firstname, employee.lastname, rowNumber);
        for (Object[] row : query().from(sub3.as(employee2)).list(employee2.firstname, employee2.lastname)){
            System.out.println(Arrays.asList(row));
        }
    }
    
}
