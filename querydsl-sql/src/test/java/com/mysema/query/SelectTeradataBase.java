package com.mysema.query;

import static com.mysema.query.Constants.survey;
import static com.mysema.query.Target.TERADATA;

import org.junit.Test;

import com.mysema.query.sql.teradata.SetQueryBandClause;
import com.mysema.testutil.IncludeIn;

public class SelectTeradataBase extends AbstractBaseTest {


    protected SetQueryBandClause setQueryBand() {
        return new SetQueryBandClause(connection, configuration);
    }

    @Test
    @IncludeIn(TERADATA)
    public void SetQueryBand_ForSession() {
        setQueryBand().set("a", "bb").forSession().execute();
        query().from(survey).list(survey.id);
    }

    @Test
    @IncludeIn(TERADATA)
    public void SetQueryBand_ForTransaction() {
        setQueryBand().set("a", "bb").forTransaction().execute();
        query().from(survey).list(survey.id);
    }

}
