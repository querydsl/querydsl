package com.mysema.query.suites;

import static com.mysema.query.sql.domain.QSurvey.survey;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Throwables;
import com.mysema.query.AbstractBaseTest;
import com.mysema.query.Connections;
import com.mysema.query.JavaSpecVersion;
import com.mysema.query.QueryException;
import com.mysema.query.sql.H2Templates;

import org.junit.BeforeClass;
import org.junit.Test;

public class H2ExceptionSuiteTest extends AbstractBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initH2();
        Connections.setTemplates(H2Templates.builder().build());

        Connections.getConnection().createStatement()
                .execute("ALTER TABLE SURVEY ADD CONSTRAINT UNIQUE_ID UNIQUE(ID)");
    }

    public static void tearDown() throws Exception {
        Connections.getConnection().createStatement()
                .execute("ALTER TABLE SURVEY DROP CONSTRAINT UNIQUE_ID");
    }

    @Test
    public void UpdateBatchFailed() {
        execute(insert(survey).columns(survey.name, survey.name2)
                .values("New Survey", "New Survey"));
        Exception result = null;
        try {
            execute(update(survey)
                    .set(survey.id, 1).addBatch()
                    .set(survey.id, 2).addBatch());
        } catch (QueryException e) {
            result = e;
        }
        assertNotNull(result);
        String stackTraceAsString = Throwables.getStackTraceAsString(result);
        switch (JavaSpecVersion.CURRENT) {
            case JAVA6:
                assertTrue(stackTraceAsString
                        .contains("Detailed SQLException information:"));
                break;
            default://Java™ 7 and higher
                assertTrue(stackTraceAsString
                        .contains("Suppressed:"));
        }
    }
}
