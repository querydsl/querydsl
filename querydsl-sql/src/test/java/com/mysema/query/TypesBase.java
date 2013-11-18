package com.mysema.query;

import static com.mysema.query.Target.CUBRID;
import static com.mysema.query.Target.POSTGRES;
import static com.mysema.query.Target.TERADATA;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.mysema.testutil.ExcludeIn;

public class TypesBase extends AbstractBaseTest {

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
