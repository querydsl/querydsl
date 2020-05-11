/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package com.querydsl.jpa.support;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * A dialect for the Teradata database created by MCR as part of the dialect
 * certification process.
 *
 * @author Jay Nance
 */
public class TeradataDialect extends Dialect {

    /**
     * Constructor
     */
    public TeradataDialect() {
        super();
        // registerColumnType data types
        registerColumnType(Types.NUMERIC, "NUMERIC($p,$s)");
        registerColumnType(Types.DOUBLE, "DOUBLE PRECISION");
        registerColumnType(Types.BIGINT, "NUMERIC(18,0)");
        registerColumnType(Types.BIT, "BYTEINT");
        registerColumnType(Types.TINYINT, "BYTEINT");
        registerColumnType(Types.VARBINARY, "VARBYTE($l)");
        registerColumnType(Types.BINARY, "BYTEINT");
        registerColumnType(Types.LONGVARCHAR, "LONG VARCHAR");
        registerColumnType(Types.CHAR, "CHAR(1)");
        registerColumnType(Types.DECIMAL, "DECIMAL");
        registerColumnType(Types.INTEGER, "INTEGER");
        registerColumnType(Types.SMALLINT, "SMALLINT");
        registerColumnType(Types.FLOAT, "FLOAT");
        registerColumnType(Types.VARCHAR, "VARCHAR($l)");
        registerColumnType(Types.DATE, "DATE");
        registerColumnType(Types.TIME, "TIME");
        registerColumnType(Types.TIMESTAMP, "TIMESTAMP");
        registerColumnType(Types.BOOLEAN, "BYTEINT"); // hibernate seems to
                                                      // ignore this type...
        registerColumnType(Types.BLOB, "BLOB");
        registerColumnType(Types.CLOB, "CLOB");

        registerFunction("year", new SQLFunctionTemplate(StandardBasicTypes.INTEGER,
                "extract(year from ?1)"));
        registerFunction("length", new SQLFunctionTemplate(StandardBasicTypes.INTEGER,
                "character_length(?1)"));
        registerFunction("concat",
                new VarArgsSQLFunction(StandardBasicTypes.STRING, "(", "||", ")"));
        registerFunction("substring", new SQLFunctionTemplate(StandardBasicTypes.STRING,
                "substring(?1 from ?2 for ?3)"));
        registerFunction("locate", new SQLFunctionTemplate(StandardBasicTypes.STRING,
                "position(?1 in ?2)"));
        registerFunction("mod", new SQLFunctionTemplate(StandardBasicTypes.STRING, "?1 mod ?2"));
        registerFunction("str", new SQLFunctionTemplate(StandardBasicTypes.STRING,
                "cast(?1 as varchar(255))"));

        // bit_length feels a bit broken to me. We have to cast to char in order
        // to
        // pass when a numeric value is supplied. But of course the answers
        // given will
        // be wildly different for these two datatypes. 1234.5678 will be 9
        // bytes as
        // a char string but will be 8 or 16 bytes as a true numeric.
        // Jay Nance 2006-09-22
        registerFunction("bit_length", new SQLFunctionTemplate(StandardBasicTypes.INTEGER,
                "octet_length(cast(?1 as char))*4"));

        // The preference here would be
        // SQLFunctionTemplate( Hibernate.TIMESTAMP, "current_timestamp(?1)",
        // false)
        // but this appears not to work.
        // Jay Nance 2006-09-22
        registerFunction("current_timestamp", new SQLFunctionTemplate(StandardBasicTypes.TIMESTAMP,
                "current_timestamp"));
        registerFunction("current_time", new SQLFunctionTemplate(StandardBasicTypes.TIME,
                "current_time"));
        registerFunction("current_date", new SQLFunctionTemplate(StandardBasicTypes.DATE,
                "current_date"));
        // IBID for current_time and current_date

        registerKeyword("account");
        registerKeyword("alias");
        registerKeyword("class");
        registerKeyword("column");
        registerKeyword("first");
        registerKeyword("map");
        registerKeyword("month");
        registerKeyword("password");
        registerKeyword("role");
        registerKeyword("summary");
        registerKeyword("title");
        registerKeyword("type");
        registerKeyword("value");
        registerKeyword("year");

        // Tell hibernate to use getBytes instead of getBinaryStream
        getDefaultProperties().setProperty(Environment.USE_STREAMS_FOR_BINARY, "false");
        // No batch statements
        getDefaultProperties().setProperty(Environment.STATEMENT_BATCH_SIZE, NO_BATCH);
    }

    /**
     * Does this dialect support the <tt>FOR UPDATE</tt> syntax?
     *
     * @return empty string ... Teradata does not support
     *         <tt>FOR UPDATE<tt> syntax
     */
    @Override
    public String getForUpdateString() {
        return "";
    }

    @Override
    public boolean supportsSequences() {
        return false;
    }

    @Override
    public String getAddColumnString() {
        return "Add";
    }

    public boolean supportsTemporaryTables() {
        return true;
    }

    public String getCreateTemporaryTableString() {
        return "create global temporary table";
    }

    public String getCreateTemporaryTablePostfix() {
        return " on commit preserve rows";
    }

    public Boolean performTemporaryTableDDLInIsolation() {
        return Boolean.TRUE;
    }

    public boolean dropTemporaryTableAfterUse() {
        return false;
    }

    /**
     * Get the name of the database type associated with the given
     * <tt>java.sql.Types</tt> typecode.
     *
     * @param code
     *            <tt>java.sql.Types</tt> typecode
     * @param length
     *            the length or precision of the column
     * @param precision
     *            the precision of the column
     * @param scale
     *            the scale of the column
     *
     * @return the database type name
     *
     * @throws HibernateException
     */
    public String getTypeName(int code, int length, int precision, int scale)
            throws HibernateException {
        /*
         * We might want a special case for 19,2. This is very common for money
         * types and here it is converted to 18,1
         */
        float f = precision > 0 ? (float) scale / (float) precision : 0;
        int p = (precision > 18 ? 18 : precision);
        int s = (precision > 18 ? (int) (18.0 * f) : (scale > 18 ? 18 : scale));

        return super.getTypeName(code, length, p, s);
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }

    @Override
    public boolean supportsCircularCascadeDeleteConstraints() {
        return false;
    }

    @Override
    public boolean areStringComparisonsCaseInsensitive() {
        return true;
    }

    @Override
    public boolean supportsEmptyInList() {
        return false;
    }

    @Override
    public String getSelectClauseNullString(int sqlType) {
        String v = "null";

        switch (sqlType) {
        case Types.BIT:
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
        case Types.FLOAT:
        case Types.REAL:
        case Types.DOUBLE:
        case Types.NUMERIC:
        case Types.DECIMAL:
            v = "cast(null as decimal)";
            break;
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            v = "cast(null as varchar(255))";
            break;
        case Types.DATE:
        case Types.TIME:
        case Types.TIMESTAMP:
            v = "cast(null as timestamp)";
            break;
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.NULL:
        case Types.OTHER:
        case Types.JAVA_OBJECT:
        case Types.DISTINCT:
        case Types.STRUCT:
        case Types.ARRAY:
        case Types.BLOB:
        case Types.CLOB:
        case Types.REF:
        case Types.DATALINK:
        case Types.BOOLEAN:
            break;
        }
        return v;
    }

    @Override
    public String getCreateMultisetTableString() {
        return "create multiset table ";
    }

    @Override
    public boolean supportsLobValueChangePropogation() {
        return false;
    }

    @Override
    public boolean doesReadCommittedCauseWritersToBlockReaders() {
        return true;
    }

    @Override
    public boolean doesRepeatableReadCauseReadersToBlockWriters() {
        return true;
    }

    @Override
    public boolean supportsBindAsCallableArgument() {
        return false;
    }

    @Override
    public boolean supportsNotNullUnique() {
        return false;
    }

    @Override
    public boolean supportsExpectedLobUsagePattern() {
        return true;
    }

    @Override
    public boolean supportsUnboundedLobLocatorMaterialization() {
        return false;
    }

    public boolean supportsDropPreProcess() {
        return true;
    }

    public String performDropPreProcess(Statement stmt, String dropSql) throws SQLException {

        String alterStr = "alter";
        String tableStr = "table";
        String dropStr = "drop";
        String constraintStr = "constraint";

        java.util.StringTokenizer st = new java.util.StringTokenizer(dropSql);
        if (alterStr.equalsIgnoreCase(st.nextToken()) && tableStr.equalsIgnoreCase(st.nextToken())) {
            String tableName = st.nextToken();

            if ((tableName.startsWith("\"")) && (!tableName.endsWith("\""))) {
                String next = null;
                while (true) {
                    next = st.nextToken();
                    tableName += " " + next;
                    if (next.endsWith("\\\"")) {
                        continue;
                    }
                    if (next.endsWith("\"")) {
                        break;
                    }
                }

            }
            if (dropStr.equalsIgnoreCase(st.nextToken())
                    && constraintStr.equalsIgnoreCase(st.nextToken())) {
                String constraintName = st.nextToken();

                // Table name might have whitespace characters within name so
                // just take whatever lies between
                // "alter table " and "drop constraint"

                int idxStart = dropSql.indexOf(tableStr, 0) + 5;
                int idxEnd = dropSql.lastIndexOf(dropStr);
                tableName = dropSql.substring(idxStart, idxEnd).trim();

                if (tableName.startsWith("\"") && tableName.endsWith("\"")) {
                    tableName = tableName.substring(1, tableName.length() - 1);
                }

                String arrStr = null;
                String queryStr = "sel IndexId, ChildTable, IndexName from dbc.RI_Distinct_ChildrenV where IndexName = '"
                        + constraintName + "'";
                java.sql.ResultSet rs = stmt.executeQuery(queryStr);

                if (rs.next()) {
                    arrStr = "drop table \"" + tableName + "_" + rs.getString(1) + "\"";
                    rs.close();
                    return arrStr;
                }
            }
        }
        return null;
    }

    public void performDropPostProcess(Statement stmt, String dropSql) throws SQLException {
        if (dropSql == null) {
            return;
        }
        stmt.executeUpdate(dropSql);
    }
}