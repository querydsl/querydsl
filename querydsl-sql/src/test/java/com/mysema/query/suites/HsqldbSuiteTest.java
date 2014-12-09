package com.mysema.query.suites;

import com.mysema.query.*;
import org.junit.BeforeClass;

import com.mysema.query.sql.HSQLDBTemplates;

public class HsqldbSuiteTest extends AbstractSuite {

    public static class BeanPopulation extends BeanPopulationBase {}
    public static class Delete extends DeleteBase {}
    public static class Insert extends InsertBase {}
    public static class LikeEscape extends LikeEscapeBase {}
    public static class Merge extends MergeBase {}
    public static class Select extends SelectBase {}
    public static class Subqueries extends SubqueriesBase {}
    public static class Types extends TypesBase {}
    public static class Union extends UnionBase {}
    public static class Update extends UpdateBase {}

    // with literals
    public static class DeleteUseLiterals extends DeleteUseLiteralsBase {}
    public static class InsertUseLiterals extends InsertUseLiteralsBase {}
    public static class MergeUseLiterals extends MergeUseLiteralsBase {}
    public static class SelectUseLiterals extends SelectUseLiteralsBase {}
    public static class UpdateUseLiterals extends UpdateUseLiteralsBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initHSQL();
        Connections.setTemplates(HSQLDBTemplates.builder().newLineToSingleSpace().build());
    }

}
