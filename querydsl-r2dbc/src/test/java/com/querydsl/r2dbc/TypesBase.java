package com.querydsl.r2dbc;

import com.google.common.collect.Maps;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.r2dbc.ddl.CreateTableClause;
import com.querydsl.r2dbc.ddl.DropTableClause;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.RelationalPathBase;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class TypesBase extends AbstractBaseTest {

    @Test
    public void create_tables() {
        Map<Class<?>, Object> instances = Maps.newLinkedHashMap();
        instances.put(BigInteger.class, BigInteger.valueOf(1));
        instances.put(Long.class, 1L);
        instances.put(Integer.class, 1);
        instances.put(Short.class, (short) 1);
        instances.put(Byte.class, (byte) 1);
        instances.put(BigDecimal.class, BigDecimal.valueOf(1.0));
        instances.put(Double.class, 1.0);
        instances.put(Float.class, 1.0f);
        instances.put(Boolean.class, Boolean.TRUE);
        instances.put(Character.class, 'a');
        instances.put(String.class, "ABC");

        for (Map.Entry<Class<?>, Object> entry : instances.entrySet()) {
            String tableName = "test_" + entry.getKey().getSimpleName();
            new DropTableClause(connection, configuration, tableName).execute();
            CreateTableClause c = new CreateTableClause(connection, configuration, tableName)
                    .column("col", entry.getKey());
            if (entry.getKey().equals(String.class)) {
                c.size(256);
            }
            c.execute();
            RelationalPath<Object> entityPath = new RelationalPathBase<Object>(Object.class, tableName, "PUBLIC", tableName);
            Path<?> columnPath = Expressions.path(entry.getKey(), entityPath, "col");
            insert(entityPath).set((Path) columnPath, entry.getValue()).execute();
            new DropTableClause(connection, configuration, tableName).execute();
        }

    }

}
