package com.mysema.query.sql.spring;

import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

public class SpringExceptionTranslator implements com.mysema.query.sql.SQLExceptionTranslator {

    private final SQLExceptionTranslator translator;
    
    public SpringExceptionTranslator() {
        this.translator = new SQLStateSQLExceptionTranslator();
    }
    
    public SpringExceptionTranslator(SQLExceptionTranslator translator) {
        this.translator = translator;        
    }
    
    @Override
    public RuntimeException translate(String sql, List<Object> bindings, SQLException e) {
        return translator.translate(null, sql, e);
    }

    @Override
    public RuntimeException translate(SQLException e) {
        return translator.translate(null, null, e);
    }

}