package com.querydsl.sql;

import static com.querydsl.core.Target.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.ddl.CreateTableClause;
import com.querydsl.sql.ddl.DropTableClause;

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

    @Test
    @ExcludeIn({CUBRID, POSTGRESQL, TERADATA})
    public void dump_types() throws SQLException {
        Connection conn = Connections.getConnection();
        DatabaseMetaData md = conn.getMetaData();

        // types
        try (ResultSet rs = md.getUDTs(null, null, null, null)) {
            while (rs.next()) {
                // cat, schema, name, classname, datatype, remarks, base_type
                String cat = rs.getString(1);
                String schema = rs.getString(2);
                String name = rs.getString(3);
                String classname = rs.getString(4);
                String datatype = rs.getString(5);
                String remarks = rs.getString(6);
                String baseType = rs.getString(7);
                System.out.println(name + " " + classname + " " + datatype + " " +
                        remarks + " " + baseType);

                // attributes
                try (ResultSet rs2 = md.getAttributes(cat, schema, name, null)) {
                    while (rs2.next()) {
                        // cat, schema, name, attr_name, data_type, attr_type_name, attr_size
                        // decimal_digits, num_prec_radix, nullable, remarks, attr_def, sql_data_type, ordinal_position
                        // ...
                        String cat2 = rs2.getString(1);
                        String schema2 = rs2.getString(2);
                        String name2 = rs2.getString(3);
                        String attrName2 = rs2.getString(4);
                        String dataType2 = rs2.getString(5);
                        String attrTypeName2 = rs2.getString(6);
                        String attrSize2 = rs2.getString(7);

                        System.out.println(" " + attrName2 + " " + dataType2 + " " + attrTypeName2 + " " + attrSize2);
                    }
                }
            }
        }
    }

}
