package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.mysema.query.BeanPopulationBase;
import com.mysema.query.Connections;
import com.mysema.query.DeleteBase;
import com.mysema.query.InsertBase;
import com.mysema.query.LikeEscapeBase;
import com.mysema.query.MergeBase;
import com.mysema.query.SelectBase;
import com.mysema.query.SelectUseLiteralsBase;
import com.mysema.query.SelectWindowFunctionsBase;
import com.mysema.query.SubqueriesBase;
import com.mysema.query.TypesBase;
import com.mysema.query.UnionBase;
import com.mysema.query.UpdateBase;
import com.mysema.query.sql.SQLServer2005Templates;
import com.mysema.testutil.ExternalDB;

@Category(ExternalDB.class)
public class MSSQLSuiteTest extends AbstractSuite {

    public static class BeanPopulation extends BeanPopulationBase {}
    public static class Delete extends DeleteBase {}
    public static class Insert extends InsertBase {}
    public static class LikeEscape extends LikeEscapeBase {}
    public static class Merge extends MergeBase {}
    public static class Select extends SelectBase {}
    public static class SelectUseLiterals extends SelectUseLiteralsBase {}
    public static class SelectWindowFunctions extends SelectWindowFunctionsBase {}
    public static class Subqueries extends SubqueriesBase {}
    public static class Types extends TypesBase {}
    public static class Union extends UnionBase {}
    public static class Update extends UpdateBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initSQLServer();
        Connections.setTemplates(SQLServer2005Templates.builder().newLineToSingleSpace().build());
    }

}
