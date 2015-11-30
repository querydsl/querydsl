package com.querydsl.sql;

import static com.querydsl.core.Target.TERADATA;
import static com.querydsl.sql.Constants.survey;

import org.junit.Test;

import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.sql.teradata.SetQueryBandClause;

public class SelectTeradataBase extends AbstractBaseTest {

    protected SetQueryBandClause setQueryBand() {
        return new SetQueryBandClause(connection, configuration);
    }

    @Test
    @IncludeIn(TERADATA)
    public void setQueryBand_forSession() {
        setQueryBand().set("a", "bb").forSession().execute();
        query().from(survey).select(survey.id).fetch();
    }

    @Test
    @IncludeIn(TERADATA)
    public void setQueryBand_forTransaction() {
        setQueryBand().set("a", "bb").forTransaction().execute();
        query().from(survey).select(survey.id).fetch();
    }

}
