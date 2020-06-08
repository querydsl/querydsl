package com.querydsl.r2dbc.suites;

import com.querydsl.core.testutil.H2;
import com.querydsl.r2dbc.*;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

@Category(H2.class)
public class H2WithQuotingTest extends AbstractSuite {

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
        Connections.initH2();
        Connections.initConfiguration(H2Templates.builder().quote().newLineToSingleSpace().build());
    }

}
