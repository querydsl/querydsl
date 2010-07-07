package com.mysema.query.sql;



public class MySQLTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new MySQLTemplates();
    }    


}
