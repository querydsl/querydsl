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
            /**
             * Super class method {@link TestQuery#uniqueResult(Expression expr)} sets limit of 2 on original SELECT
             * and results in <tt>select min(e.SALARY) from EMPLOYEE e limit ?</tt> statement. NuoDB uses natural
             * ordering by default and for this query specifically, which might cause min() value to be not among
             * requested rows causes {@link #Aggregate_UniqueResult()} test.
             */
            TestQuery testQuery = new TestQuery(connection, configuration) {
                @Override
                public <RT> RT uniqueResult(Expression<RT> expr) {
                    CloseableIterator<RT> iterator = iterate(expr);
                    return super.uniqueResult(iterator);
                }
            };
            testQuery.addListener(new TestLoggingListener());
            return testQuery;
        }

        /**
         * Aggregate unique result is fixed in overridden {@link TestQuery#uniqueResult(Expression expr)}.
         */
        @Override
        public void Aggregate_UniqueResult() {
            super.Aggregate_UniqueResult();
        }
    }
    public static class Subqueries extends SubqueriesBase {}
    public static class Types extends TypesBase {}
    public static class Union extends UnionBase {}
    public static class Update extends UpdateBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initNuoDB();
        Connections.initConfiguration(NuoDBTemplates.builder().build());
    }
}