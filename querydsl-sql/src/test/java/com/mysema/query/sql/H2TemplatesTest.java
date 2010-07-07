package com.mysema.query.sql;

public class H2TemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new H2Templates();
    }

}
