package com.mysema.query.sql;



public class OracleTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new OracleTemplates();
    }    


}
