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
import com.querydsl.sql.ddl.CreateTableClause;
import com.querydsl.sql.ddl.DropTableClause;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.Path;
import com.querydsl.core.testutil.ExcludeIn;

public class TypesBase extends AbstractBaseTest {

    @Test
    public void CreateTables() {
        Map<Class<?>, Object> instances = Maps.newLinkedHashMap();
        instances.put(BigInteger.class, BigInteger.valueOf(1));
        instances.put(Long.class, Long.valueOf(1));
        instances.put(Integer.class, Integer.valueOf(1));
        instances.put(Short.class, Short.valueOf((short)1));
        instances.put(Byte.class, Byte.valueOf((byte)1));
        instances.put(BigDecimal.class, BigDecimal.valueOf(1.0));
        instances.put(Double.class, Double.valueOf(1.0));
        instances.put(Float.class, Float.valueOf((float)1.0));
        instances.put(Boolean.class, Boolean.TRUE);
        instances.put(Character.class, Character.valueOf('a'));
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
            insert(entityPath).set((Path)columnPath, entry.getValue()).execute();
            new DropTableClause(connection, configuration, tableName).execute();
        }

    }

    @Test
    @ExcludeIn({CUBRID, POSTGRES, TERADATA})
    public void DumpTypes() throws SQLException {
        Connection conn = Connections.getConnection();
        DatabaseMetaData md = conn.getMetaData();

        // types
        ResultSet rs = md.getUDTs(null, null, null, null);
        try {
            while (rs.next()) {
                // cat, schema, name, classname, datatype, remarks, base_type
                String cat = rs.getString(1);
                String schema = rs.getString(2);
                String name = rs.getString(3);
                String classname = rs.getString(4);
                String datatype = rs.getString(5);
                String remarks = rs.getString(6);
                String base_type = rs.getString(7);
                System.out.println(name + " " + classname + " " + datatype + " " +
                                   remarks + " " + base_type);

                // attributes
                ResultSet rs2 = md.getAttributes(cat, schema, name, null);
                try {
                    while (rs2.next()) {
                        // cat, schema, name, attr_name, data_type, attr_type_name, attr_size
                        // decimal_digits, num_prec_radix, nullable, remarks, attr_def, sql_data_type, ordinal_position
                        // ...
                        String _cat = rs2.getString(1);
                        String _schema = rs2.getString(2);
                        String _name = rs2.getString(3);
                        String _attr_name = rs2.getString(4);
                        String _data_type = rs2.getString(5);
                        String _attr_type_name = rs2.getString(6);
                        String _attr_size = rs2.getString(7);

                        System.out.println(" " + _attr_name + " " + _data_type + " " + _attr_type_name + " " + _attr_size);
                    }
                } finally {
                    rs2.close();
                }
            }
        } finally {
            rs.close();
        }
    }

}
