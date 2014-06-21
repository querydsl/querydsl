package com.mysema.query.suites;

import com.mysema.query.*;
import com.mysema.query.sql.FirebirdTemplates;
import com.mysema.testutil.ExternalDB;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

@Category(ExternalDB.class)
public class FirebirdSuiteTest extends AbstractSuite {

    public static class BeanPopulation extends BeanPopulationBase {}
    public static class Delete extends DeleteBase {}
    public static class Insert extends InsertBase {}
    public static class LikeEscape extends LikeEscapeBase {}
    public static class Merge extends MergeBase {}
    public static class Select extends SelectBase {}
    public static class SelectUseLiterals extends SelectUseLiteralsBase {}
    public static class Subqueries extends SubqueriesBase {}
    public static class Types extends TypesBase {}
    //public static class Union extends UnionBase {}
    public static class Update extends UpdateBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initFirebird();
        Connections.setTemplates(FirebirdTemplates.builder().newLineToSingleSpace().build());
    }
}
