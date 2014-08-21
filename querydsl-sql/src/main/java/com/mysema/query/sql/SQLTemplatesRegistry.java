package com.mysema.query.sql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 *
 */
public class SQLTemplatesRegistry {

    private final Map<String, SQLTemplates> registry = Maps.newHashMap();

    public SQLTemplatesRegistry() {
        registry.put("h2", new H2Templates());
        registry.put("postgresql", new PostgresTemplates());
        registry.put("oracle", new OracleTemplates());
        registry.put("mysql", new MySQLTemplates());
        registry.put("hsql", new HSQLDBTemplates());
        registry.put("firebird", new FirebirdTemplates());
        registry.put("sqlite", new SQLiteTemplates());
        registry.put("apache", new DerbyTemplates());
        // TODO SQLServer
        // TODO Teradata
    }

    public SQLTemplates getTemplates(DatabaseMetaData md) {
        try {
            String name = md.getDatabaseProductName();
            int separator = name.indexOf(' ');
            if (separator > -1) {
                name = name.substring(0, separator);
            }
            return registry.get(name.toLowerCase());
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
