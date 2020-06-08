package com.querydsl.r2dbc.suites;

import com.querydsl.core.testutil.PostgreSQL;
import com.querydsl.r2dbc.*;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

@Category(PostgreSQL.class)
public class PostgreSQLLiteralsSuiteTest extends AbstractSuite {

    public static class BeanPopulation extends BeanPopulationBase {
    }

    public static class Delete extends DeleteBase {
    }

    public static class Insert extends InsertBase {
    }

    public static class KeywordQuoting extends KeywordQuotingBase {

        private Configuration previous;

        @Override
        public void setUp() throws Exception {
            //NOTE: replacing the templates with a non-quoting one
            previous = configuration;
            configuration = new Configuration(PostgreSQLTemplates.builder().newLineToSingleSpace().build());
            super.setUp();
        }

        @Override
        public void tearDown() throws Exception {
            super.tearDown();
            //NOTE: restoring old templates
            configuration = previous;
        }

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
        Connections.initPostgreSQL();
        Connections.initConfiguration(PostgreSQLTemplates.builder().quote().newLineToSingleSpace().build());
        Connections.getConfiguration().setUseLiterals(true);
    }

}
