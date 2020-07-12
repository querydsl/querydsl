package com.querydsl.r2dbc.suites;

import com.querydsl.core.testutil.SQLServer;
import com.querydsl.r2dbc.*;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

@Category(SQLServer.class)
public class MSSQLSuiteTest extends AbstractSuite {

    public static class BeanPopulation extends BeanPopulationBase {
    }

    public static class Delete extends DeleteBase {
    }

    public static class Insert extends InsertBase {
    }

    public static class KeywordQuoting extends KeywordQuotingBase {
    }

    public static class LikeEscape extends LikeEscapeBase {
    }

    public static class Select extends SelectBase {
    }

    public static class SelectWindowFunctions extends SelectWindowFunctionsBase {
    }

    public static class Subqueries extends SubqueriesBase {
    }

    public static class Types extends TypesBase {
    }

    public static class Union extends UnionBase {
    }

    public static class Update extends UpdateBase {
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initConfiguration(SQLServer2008Templates.builder().newLineToSingleSpace().build());
        Connections.initSQLServer();
    }

}
