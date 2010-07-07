package com.mysema.query.sql;



public class PostgresTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new PostgresTemplates();
    }    


}
