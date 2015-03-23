package com.mysema.query.suites;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.NuoDBTemplates;
import com.mysema.query.sql.spatial.MySQLSpatialTemplates;
import com.mysema.query.types.Expression;
import com.mysema.query.*;
import com.mysema.testutil.ExternalDB;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

/**
 * @author Sergey Bushik
 */
@Category(ExternalDB.class)
public class NuoDBSuiteTest extends AbstractSuite {

    public static class BeanPopulation extends BeanPopulationBase {}
    public static class Delete extends DeleteBase {}
    public static class Insert extends InsertBase {}
    public static class LikeEscape extends LikeEscapeBase {}
    public static class Merge extends MergeBase {}
    public static class Select extends SelectBase {

        protected TestQuery query() {
            TestQuery testQuery = new TestQuery(connection, configuration) {
                @Override
                public <RT> RT uniqueResult(Expression<RT> expr) {
                    CloseableIterator<RT> iterator = iterate(expr);
                    return uniqueResult(iterator);
                }
            };
            return testQuery;
        }

        @Override
        public void Aggregate_UniqueResult() {
            super.Aggregate_UniqueResult();
        }
    }
    public static class SelectWindowFunctions extends SelectWindowFunctionsBase {}
    public static class Subqueries extends SubqueriesBase {}
    public static class Types extends TypesBase {}
    public static class Union extends UnionBase {}
    public static class Update extends UpdateBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initNuoDB();
        Connections.setTemplates(NuoDBTemplates.builder().build());
    }
}