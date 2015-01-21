package com.querydsl.sql;

import static com.querydsl.sql.Constants.survey;
import static com.querydsl.core.Target.TERADATA;

import org.junit.Test;

import com.querydsl.sql.teradata.SetQueryBandClause;
import com.querydsl.core.testutil.IncludeIn;

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
