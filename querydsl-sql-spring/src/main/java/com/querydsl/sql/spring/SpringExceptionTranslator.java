/*
 * Copyright 2014, Timo Westkämper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.spring;

import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

/**
 * SpringExceptionTranslator is an SQLExceptionTranslator implementation which uses Spring's
 * exception translation functionality internally
 *
 * <p>Usage example</p>
 * <pre>
 * {@code
 * Configuration configuration = new Configuration(templates);
 * configuration.setExceptionTranslator(new SpringExceptionTranslator());
 * }
 * </pre>
 *
 */
public class SpringExceptionTranslator implements com.querydsl.sql.SQLExceptionTranslator {

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