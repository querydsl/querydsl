/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.sql;

import static com.google.common.base.CharMatcher.inRange;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryException;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.sql.types.Type;
import com.querydsl.core.types.*;

/**
 * SQLTemplates extends Templates to provides SQL specific extensions
 * and acts as database specific Dialect for Querydsl SQL
 *
 * @author tiwe
 */
public class SQLTemplates extends Templates {

    protected static final Set<String> SQL_RESERVED_WORDS
            = ImmutableSet.of(
                    "ABS", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE",
                    "ARRAY", "ARRAY_AGG", "AS", "ASENSITIVE", "ASYMMETRIC",
                    "AT", "ATOMIC", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN",
                    "BIGINT", "BINARY", "BLOB", "BOOLEAN", "BOTH", "BY", "CALL",
                    "CALLED", "CARDINALITY", "CASCADED", "CASE", "CAST", "CEIL",
                    "CEILING", "CHAR", "CHARACTER", "CHARACTER_LENGTH",
                    "CHAR_LENGTH", "CHECK", "CLOB", "CLOSE", "COALESCE",
                    "COLLATE", "COLLECT", "COLUMN", "COMMIT", "CONDITION",
                    "CONNECT", "CONSTRAINT", "CONVERT", "CORR", "CORRESPONDING",
                    "COUNT", "COVAR_POP", "COVAR_SAMP", "CREATE", "CROSS",
                    "CUBE", "CUME_DIST", "CURRENT", "CURRENT_CATALOG",
                    "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP",
                    "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_SCHEMA",
                    "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE",
                    "CURRENT_USER", "CURSOR", "CYCLE", "DATE", "DAY",
                    "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT",
                    "DELETE", "DENSE_RANK", "DEREF", "DESCRIBE",
                    "DETERMINISTIC", "DISCONNECT", "DISTINCT", "DOUBLE",
                    "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "END",
                    "END-EXEC", "ESCAPE", "EVERY", "EXCEPT", "EXEC", "EXECUTE",
                    "EXISTS", "EXP", "EXTERNAL", "EXTRACT", "FALSE", "FETCH",
                    "FILTER", "FIRST_VALUE", "FLOAT", "FLOOR", "FOR", "FOREIGN",
                    "FREE", "FROM", "FULL", "FUNCTION", "FUSION", "GET",
                    "GLOBAL", "GRANT", "GROUP", "GROUPING", "HAVING", "HOLD",
                    "HOUR", "IDENTITY", "IN", "INDICATOR", "INNER", "INOUT",
                    "INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT",
                    "INTERSECTION", "INTERVAL", "INTO", "IS", "JOIN", "LAG",
                    "LANGUAGE", "LARGE", "LAST_VALUE", "LATERAL", "LEAD",
                    "LEADING", "LEFT", "LIKE", "LIKE_REGEX", "LN", "LOCAL",
                    "LOCALTIME", "LOCALTIMESTAMP", "LOWER", "MATCH", "MAX",
                    "MAX_CARDINALITY", "MEMBER", "MERGE", "METHOD", "MIN",
                    "MINUTE", "MOD", "MODIFIES", "MODULE", "MONTH", "MULTISET",
                    "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NEW", "NO",
                    "NONE", "NORMALIZE", "NOT", "NTH_VALUE", "NTILE", "NULL",
                    "NULLIF", "NUMERIC", "OCCURRENCES_REGEX", "OCTET_LENGTH",
                    "OF", "OFFSET", "OLD", "ON", "ONLY", "OPEN", "OR", "ORDER",
                    "OUT", "OUTER", "OVER", "OVERLAPS", "OVERLAY", "PARAMETER",
                    "PARTITION", "PERCENTILE_CONT", "PERCENTILE_DISC",
                    "PERCENT_RANK", "POSITION", "POSITION_REGEX", "POWER",
                    "PRECISION", "PREPARE", "PRIMARY", "PROCEDURE", "RANGE",
                    "RANK", "READS", "REAL", "RECURSIVE", "REF", "REFERENCES",
                    "REFERENCING", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT",
                    "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX",
                    "REGR_SXY", "REGR_SYY", "RELEASE", "RESULT", "RETURN",
                    "RETURNS", "REVOKE", "RIGHT", "ROLLBACK", "ROLLUP",
                    "ROW", "ROWS", "ROW_NUMBER", "SAVEPOINT", "SCOPE", "SCROLL",
                    "SEARCH", "SECOND", "SELECT", "SENSITIVE", "SESSION_USER",
                    "SET", "SIMILAR", "SMALLINT", "SOME", "SPECIFIC",
                    "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE",
                    "SQLWARNING", "SQRT", "START", "STATIC", "STDDEV_POP",
                    "STDDEV_SAMP", "SUBMULTISET", "SUBSTRING", "SUBSTRING_REGEX",
                    "SUM", "SYMMETRIC", "SYSTEM", "SYSTEM_USER", "TABLE",
                    "TABLESAMPLE", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR",
                    "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSLATE",
                    "TRANSLATE_REGEX", "TRANSLATION", "TREAT", "TRIGGER", "TRIM",
                    "TRIM_ARRAY", "TRUE", "TRUNCATE", "UESCAPE", "UNION",
                    "UNIQUE", "UNKNOWN", "UNNEST", "UPDATE", "UPPER", "USER",
                    "USING", "VALUE", "VALUES", "VARBINARY", "VARCHAR",
                    "VARYING", "VAR_POP", "VAR_SAMP", "WHEN", "WHENEVER",
                    "WHERE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN",
                    "WITHOUT", "XML", "XMLAGG", "XMLATTRIBUTES", "XMLBINARY",
                    "XMLCAST", "XMLCOMMENT", "XMLCONCAT", "XMLDOCUMENT",
                    "XMLELEMENT", "XMLEXISTS", "XMLFOREST", "XMLITERATE",
                    "XMLNAMESPACES", "XMLPARSE", "XMLPI", "XMLQUERY",
                    "XMLSERIALIZE", "XMLTABLE", "XMLTEXT", "XMLVALIDATE", "YEAR");

    public static final Expression<?> RECURSIVE = TemplateExpressionImpl.create(Object.class, "");

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final SQLTemplates DEFAULT = new SQLTemplates("\"",'\\',false);

    private static final CharMatcher NON_UNDERSCORE_ALPHA_NUMERIC =
            CharMatcher.is('_').or(inRange('a', 'z').or(inRange('A', 'Z'))).or(inRange('0', '9'))
            .negate().precomputed();

    private final Set<String> reservedWords;

    public abstract static class Builder {

        protected boolean printSchema, quote, newLineToSingleSpace;

        protected char escape = '\\';

        public Builder printSchema() {
            printSchema = true;
            return this;
        }

        public Builder quote() {
            quote = true;
            return this;
        }

        public Builder newLineToSingleSpace() {
            newLineToSingleSpace = true;
            return this;
        }

        public Builder escape(char ch) {
            escape = ch;
            return this;
        }

        protected abstract SQLTemplates build(char escape, boolean quote);

        public SQLTemplates build() {
            SQLTemplates templates = build(escape, quote);
            if (newLineToSingleSpace) {
                templates.newLineToSingleSpace();
            }
            templates.setPrintSchema(printSchema);
            return templates;
        }

    }

    private final Map<String, Integer> typeNameToCode = Maps.newHashMap();

    private final Map<Integer, String> codeToTypeName = Maps.newHashMap();

    private final Map<SchemaAndTable, SchemaAndTable> tableOverrides = Maps.newHashMap();

    private final List<Type<?>> customTypes = Lists.newArrayList();

    private final String quoteStr;

    private final boolean useQuotes;

    private boolean printSchema;

    private String createTable = "create table ";

    private String asc = " asc";

    private String autoIncrement = " auto_increment";

    private String columnAlias = " ";

    private String count = "count ";

    private String countStar = "count(*)";

    private String delete = "delete ";

    private String desc = " desc";

    private String distinctCountEnd = ")";

    private String distinctCountStart = "count(distinct ";

    private String dummyTable = "dual";

    private String from = "\nfrom ";

    private String fullJoin = "\nfull join ";

    private String groupBy = "\ngroup by ";

    private String having = "\nhaving ";

    private String innerJoin = "\ninner join ";

    private String insertInto = "insert into ";

    private String join = "\njoin ";

    private String key = "key";

    private String leftJoin = "\nleft join ";

    private String rightJoin = "\nright join ";;

    private String limitTemplate = "\nlimit {0}";

    private String mergeInto = "merge into ";

    private boolean nativeMerge;

    private String notNull = " not null";

    private String offsetTemplate = "\noffset {0}";

    private String on = "\non ";

    private String orderBy = "\norder by ";

    private String select = "select ";

    private String selectDistinct = "select distinct ";

    private String set = "set ";

    private String tableAlias = " ";

    private String update = "update ";

    private String values = "\nvalues ";

    private String defaultValues = "\nvalues ()";

    private String where = "\nwhere ";

    private String with = "with ";

    private String withRecursive = "with recursive ";

    private String createIndex = "create index ";

    private String createUniqueIndex = "create unique index ";

    private String nullsFirst = " nulls first";

    private String nullsLast = " nulls last";

    private boolean parameterMetadataAvailable = true;

    private boolean batchCountViaGetUpdateCount = false;

    private boolean unionsWrapped = true;

    private boolean functionJoinsWrapped = false;

    private boolean limitRequired = false;

    private boolean countDistinctMultipleColumns = false;

    private boolean countViaAnalytics = false;

    private boolean wrapSelectParameters = false;

    private boolean arraysSupported = true;

    private int listMaxSize = 0;

    private boolean supportsUnquotedReservedWordsAsIdentifier = false;

    @Deprecated
    protected SQLTemplates(String quoteStr, char escape, boolean useQuotes) {
        this(SQL_RESERVED_WORDS, quoteStr, escape, useQuotes);
    }

    protected SQLTemplates(Set<String> reservedKeywords, String quoteStr, char escape, boolean useQuotes) {
        super(escape);
        this.reservedWords = reservedKeywords;
        this.quoteStr = quoteStr;
        this.useQuotes = useQuotes;

        add(SQLOps.ALL, "{0}.*");

        // flags
        add(SQLOps.WITH_ALIAS, "{0} as {1}", 0);
        add(SQLOps.WITH_COLUMNS, "{0} {1}", 0);
        add(SQLOps.FOR_UPDATE, "\nfor update");
        add(SQLOps.FOR_SHARE, "\nfor share");
        add(SQLOps.NO_WAIT, " nowait");
        add(SQLOps.QUALIFY, "\nqualify {0}");

        // boolean
        add(Ops.AND, "{0} and {1}", 36);
        add(Ops.NOT, "not {0}", 35);
        add(Ops.OR, "{0} or {1}", 38);
        add(Ops.XNOR, "{0} xnor {1}", 39);
        add(Ops.XOR, "{0} xor {1}", 39);

        // math
        add(Ops.MathOps.RANDOM, "rand()");
        add(Ops.MathOps.RANDOM2, "rand({0})");
        add(Ops.MathOps.CEIL, "ceiling({0})");
        add(Ops.MathOps.POWER, "power({0},{1})");
        add(Ops.MOD, "mod({0},{1})", -1);

        // date time
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_time");
        add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp");

        add(Ops.DateTimeOps.MILLISECOND, "0");
        add(Ops.DateTimeOps.SECOND, "extract(second from {0})");
        add(Ops.DateTimeOps.MINUTE, "extract(minute from {0})");
        add(Ops.DateTimeOps.HOUR, "extract(hour from {0})");
        add(Ops.DateTimeOps.WEEK, "extract(week from {0})");
        add(Ops.DateTimeOps.MONTH, "extract(month from {0})");
        add(Ops.DateTimeOps.YEAR, "extract(year from {0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "(extract(year from {0}) * 100 + extract(month from {0}))");
        add(Ops.DateTimeOps.YEAR_WEEK, "(extract(year from {0}) * 100 + extract(week from {0}))");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "extract(day_of_week from {0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "extract(day from {0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "extract(day_of_year from {0})");

        add(Ops.DateTimeOps.ADD_YEARS, "dateadd('year',{1},{0})");
        add(Ops.DateTimeOps.ADD_MONTHS, "dateadd('month',{1},{0})");
        add(Ops.DateTimeOps.ADD_WEEKS, "dateadd('week',{1},{0})");
        add(Ops.DateTimeOps.ADD_DAYS, "dateadd('day',{1},{0})");
        add(Ops.DateTimeOps.ADD_HOURS, "dateadd('hour',{1},{0})");
        add(Ops.DateTimeOps.ADD_MINUTES, "dateadd('minute',{1},{0})");
        add(Ops.DateTimeOps.ADD_SECONDS, "dateadd('second',{1},{0})");

        add(Ops.DateTimeOps.DIFF_YEARS, "datediff('year',{0},{1})");
        add(Ops.DateTimeOps.DIFF_MONTHS, "datediff('month',{0},{1})");
        add(Ops.DateTimeOps.DIFF_WEEKS, "datediff('week',{0},{1})");
        add(Ops.DateTimeOps.DIFF_DAYS, "datediff('day',{0},{1})");
        add(Ops.DateTimeOps.DIFF_HOURS, "datediff('hour',{0},{1})");
        add(Ops.DateTimeOps.DIFF_MINUTES, "datediff('minute',{0},{1})");
        add(Ops.DateTimeOps.DIFF_SECONDS, "datediff('second',{0},{1})");

        add(Ops.DateTimeOps.TRUNC_YEAR, "date_trunc('year',{0})");
        add(Ops.DateTimeOps.TRUNC_MONTH, "date_trunc('month',{0})");
        add(Ops.DateTimeOps.TRUNC_WEEK, "date_trunc('week',{0})");
        add(Ops.DateTimeOps.TRUNC_DAY, "date_trunc('day',{0})");
        add(Ops.DateTimeOps.TRUNC_HOUR, "date_trunc('hour',{0})");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "date_trunc('minute',{0})");
        add(Ops.DateTimeOps.TRUNC_SECOND, "date_trunc('second',{0})");

        // string
        add(Ops.CONCAT, "{0} || {1}", 38);
        add(Ops.MATCHES, "{0} regexp {1}", 25);
        add(Ops.CHAR_AT, "cast(substr({0},{1s}+1,1) as char)");
        add(Ops.EQ_IGNORE_CASE, "{0l} = {1l}");
        add(Ops.INDEX_OF, "locate({1},{0})-1");
        add(Ops.INDEX_OF_2ARGS, "locate({1},{0},{2s}+1)-1");
        add(Ops.STRING_IS_EMPTY, "length({0}) = 0");
        add(Ops.SUBSTR_1ARG, "substr({0},{1s}+1)", 1);
        add(Ops.SUBSTR_2ARGS, "substr({0},{1s}+1,{2s}-{1s})", 1);
        add(Ops.StringOps.LOCATE, "locate({0},{1})");
        add(Ops.StringOps.LOCATE2, "locate({0},{1},{2})");

        // like with escape
        add(Ops.LIKE, "{0} like {1} escape '"+escape+"'");
        add(Ops.ENDS_WITH, "{0} like {%1} escape '"+escape+"'");
        add(Ops.ENDS_WITH_IC, "{0l} like {%%1} escape '"+escape+"'");
        add(Ops.STARTS_WITH, "{0} like {1%} escape '"+escape+"'");
        add(Ops.STARTS_WITH_IC, "{0l} like {1%%} escape '"+escape+"'");
        add(Ops.STRING_CONTAINS, "{0} like {%1%} escape '"+escape+"'");
        add(Ops.STRING_CONTAINS_IC, "{0l} like {%%1%%} escape '"+escape+"'");

        add(SQLOps.CAST, "cast({0} as {1s})");
        add(SQLOps.UNION, "{0}\nunion\n{1}", 50);
        add(SQLOps.UNION_ALL, "{0}\nunion all\n{1}", 50);
        add(SQLOps.NEXTVAL, "nextval('{0s}')");

        // analytic functions
        add(SQLOps.CORR, "corr({0},{1})");
        add(SQLOps.COVARPOP, "covar_pop({0},{1})");
        add(SQLOps.COVARSAMP, "covar_samp({0},{1})");
        add(SQLOps.CUMEDIST, "cume_dist()");
        add(SQLOps.CUMEDIST2, "cume_dist({0})");
        add(SQLOps.DENSERANK, "dense_rank()");
        add(SQLOps.DENSERANK2, "dense_rank({0})");
        add(SQLOps.FIRSTVALUE, "first_value({0})");
        add(SQLOps.LAG, "lag({0})");
        add(SQLOps.LASTVALUE, "last_value({0})");
        add(SQLOps.LEAD, "lead({0})");
        add(SQLOps.LISTAGG, "listagg({0},'{1s}')");
        add(SQLOps.NTHVALUE, "nth_value({0}, {1})");
        add(SQLOps.NTILE, "ntile({0})");
        add(SQLOps.PERCENTILECONT, "percentile_cont({0})");
        add(SQLOps.PERCENTILEDISC, "percentile_disc({0})");
        add(SQLOps.PERCENTRANK, "percent_rank()");
        add(SQLOps.PERCENTRANK2, "percent_rank({0})");
        add(SQLOps.RANK, "rank()");
        add(SQLOps.RANK2, "rank({0})");
        add(SQLOps.RATIOTOREPORT, "ratio_to_report({0})");
        add(SQLOps.REGR_SLOPE, "regr_slope({0}, {1})");
        add(SQLOps.REGR_INTERCEPT, "regr_intercept({0}, {1})");
        add(SQLOps.REGR_COUNT, "regr_count({0}, {1})");
        add(SQLOps.REGR_R2, "regr_r2({0}, {1})");
        add(SQLOps.REGR_AVGX, "regr_avgx({0}, {1})");
        add(SQLOps.REGR_AVGY, "regr_avgy({0}, {1})");
        add(SQLOps.REGR_SXX, "regr_sxx({0}, {1})");
        add(SQLOps.REGR_SYY, "regr_syy({0}, {1})");
        add(SQLOps.REGR_SXY, "regr_sxy({0}, {1})");
        add(SQLOps.ROWNUMBER, "row_number()");
        add(SQLOps.STDDEV, "stddev({0})");
        add(SQLOps.STDDEVPOP, "stddev_pop({0})");
        add(SQLOps.STDDEVSAMP, "stddev_samp({0})");
        add(SQLOps.STDDEV_DISTINCT, "stddev(distinct {0})");
        add(SQLOps.VARIANCE, "variance({0})");
        add(SQLOps.VARPOP, "var_pop({0})");
        add(SQLOps.VARSAMP, "var_samp({0})");

        add(Ops.AggOps.BOOLEAN_ANY, "some({0})");
        add(Ops.AggOps.BOOLEAN_ALL, "every({0})");

        // default type names
        addTypeNameToCode("null", Types.NULL);
        addTypeNameToCode("char", Types.CHAR);
        addTypeNameToCode("datalink", Types.DATALINK);
        addTypeNameToCode("numeric", Types.NUMERIC);
        addTypeNameToCode("decimal", Types.DECIMAL);
        addTypeNameToCode("integer", Types.INTEGER);
        addTypeNameToCode("smallint", Types.SMALLINT);
        addTypeNameToCode("float", Types.FLOAT);
        addTypeNameToCode("real", Types.REAL);
        addTypeNameToCode("double", Types.DOUBLE);
        addTypeNameToCode("varchar", Types.VARCHAR);
        addTypeNameToCode("longnvarchar", Types.LONGNVARCHAR);
        addTypeNameToCode("nchar", Types.NCHAR);
        addTypeNameToCode("boolean", Types.BOOLEAN);
        addTypeNameToCode("nvarchar", Types.NVARCHAR);
        addTypeNameToCode("rowid", Types.ROWID);
        addTypeNameToCode("timestamp", Types.TIMESTAMP);
        addTypeNameToCode("bit", Types.BIT);
        addTypeNameToCode("time", Types.TIME);
        addTypeNameToCode("tinyint", Types.TINYINT);
        addTypeNameToCode("other", Types.OTHER);
        addTypeNameToCode("bigint", Types.BIGINT);
        addTypeNameToCode("longvarbinary", Types.LONGVARBINARY);
        addTypeNameToCode("varbinary", Types.VARBINARY);
        addTypeNameToCode("date", Types.DATE);
        addTypeNameToCode("binary", Types.BINARY);
        addTypeNameToCode("longvarchar", Types.LONGVARCHAR);
        addTypeNameToCode("struct", Types.STRUCT);
        addTypeNameToCode("array", Types.ARRAY);
        addTypeNameToCode("java_object", Types.JAVA_OBJECT);
        addTypeNameToCode("distinct", Types.DISTINCT);
        addTypeNameToCode("ref", Types.REF);
        addTypeNameToCode("blob", Types.BLOB);
        addTypeNameToCode("clob", Types.CLOB);
        addTypeNameToCode("nclob", Types.NCLOB);
        addTypeNameToCode("sqlxml", Types.SQLXML);
    }

    public String serialize(String literal, int jdbcType) {
        switch (jdbcType) {
            case Types.TIMESTAMP:
                return "(timestamp '" + literal + "')";
            case Types.DATE:
                return "(date '" + literal + "')";
            case Types.TIME:
                return "(time '" + literal + "')";
            case Types.CHAR:
            case Types.CLOB:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NCLOB:
            case Types.NVARCHAR:
            case Types.VARCHAR:
                return "'" + escapeLiteral(literal) + "'";
            case Types.BIGINT:
            case Types.BIT:
            case Types.BOOLEAN:
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.INTEGER:
            case Types.NULL:
            case Types.NUMERIC:
            case Types.SMALLINT:
            case Types.TINYINT:
                return literal;                        
            default:
                // for other JDBC types the Type instance is expected to provide
                // the necessary quoting
                return literal;
        }
    }

    public String escapeLiteral(String str) {
        StringBuilder builder = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (ch == '\n') {
                builder.append("\\n");
                continue;
            } else if (ch == '\r') {
                builder.append("\\r");
                continue;
            } else if (ch == '\'') {
                builder.append("''");
                continue;
            }
            builder.append(ch);
        }
        return builder.toString();
    }

    protected void addTypeNameToCode(String type, int code, boolean override) {
        if (!typeNameToCode.containsKey(type)) {
            typeNameToCode.put(type, code);
        }
        if (override || !codeToTypeName.containsKey(code)) {
            codeToTypeName.put(code, type);
        }
    }

    protected void addTypeNameToCode(String type, int code) {
        addTypeNameToCode(type, code, false);
    }

    protected void addTableOverride(SchemaAndTable from, SchemaAndTable to) {
        tableOverrides.put(from, to);
    }

    /**
     * Use customTypes instead
     */
    @Deprecated
    public final boolean isBigDecimalSupported() {
        return false;
    }

    public final List<Type<?>> getCustomTypes() {
        return customTypes;
    }

    public final String getAsc() {
        return asc;
    }

    public final String getAutoIncrement() {
        return autoIncrement;
    }

    public final String getColumnAlias() {
        return columnAlias;
    }

    public final String getCount() {
        return count;
    }

    public final String getCountStar() {
        return countStar;
    }

    public final String getDelete() {
        return delete;
    }

    public final String getDesc() {
        return desc;
    }

    public final String getDistinctCountEnd() {
        return distinctCountEnd;
    }

    public final String getDistinctCountStart() {
        return distinctCountStart;
    }

    public final String getDummyTable() {
        return dummyTable;
    }

    public final String getFrom() {
        return from;
    }

    public final String getFullJoin() {
        return fullJoin;
    }

    public final String getGroupBy() {
        return groupBy;
    }

    public final String getHaving() {
        return having;
    }

    public final String getInnerJoin() {
        return innerJoin;
    }

    public final String getInsertInto() {
        return insertInto;
    }

    public final String getJoin() {
        return join;
    }

    public final String getJoinSymbol(JoinType joinType) {
        switch (joinType) {
            case JOIN:      return join;
            case INNERJOIN: return innerJoin;
            case FULLJOIN:  return fullJoin;
            case LEFTJOIN:  return leftJoin;
            case RIGHTJOIN: return rightJoin;
            default:       return ", ";
        }
    }

    public final String getKey() {
        return key;
    }

    public final String getLeftJoin() {
        return leftJoin;
    }

    public final String getRightJoin() {
        return rightJoin;
    }

    public final String getLimitTemplate() {
        return limitTemplate;
    }

    public final String getMergeInto() {
        return mergeInto;
    }

    public final String getNotNull() {
        return notNull;
    }

    public final String getOffsetTemplate() {
        return offsetTemplate;
    }

    public final String getOn() {
        return on;
    }

    public final String getOrderBy() {
        return orderBy;
    }

    public final String getSelect() {
        return select;
    }

    public final String getSelectDistinct() {
        return selectDistinct;
    }

    public final String getSet() {
        return set;
    }

    public final String getTableAlias() {
        return tableAlias;
    }

    public final Map<SchemaAndTable, SchemaAndTable> getTableOverrides() {
        return tableOverrides;
    }

    @Deprecated
    public String getTypeForCast(Class<?> cl) {
        return getTypeForClass(cl);
    }

    @Deprecated
    public String getTypeForClass(Class<?> cl) {
        throw new UnsupportedOperationException();
    }

    public String getTypeNameForCode(int code) {
        return codeToTypeName.get(code);
    }

    public String getCastTypeNameForCode(int code) {
        return getTypeNameForCode(code);
    }

    public Integer getCodeForTypeName(String type) {
        return typeNameToCode.get(type);
    }

    public final String getUpdate() {
        return update;
    }

    public final String getValues() {
        return values;
    }

    public final String getDefaultValues() {
        return defaultValues;
    }

    public final String getWhere() {
        return where;
    }

    public final boolean isNativeMerge() {
        return nativeMerge;
    }

    public final boolean isSupportsAlias() {
        return true;
    }

    public final String getCreateIndex() {
        return createIndex;
    }

    public final String getCreateUniqueIndex() {
        return createUniqueIndex;
    }

    public final String getCreateTable() {
        return createTable;
    }

    public final String getWith() {
        return with;
    }

    public final String getWithRecursive() {
        return withRecursive;
    }

    public final boolean isCountDistinctMultipleColumns() {
        return countDistinctMultipleColumns;
    }

    public final boolean isPrintSchema() {
        return printSchema;
    }

    public final boolean isParameterMetadataAvailable() {
        return parameterMetadataAvailable;
    }

    public final boolean isBatchCountViaGetUpdateCount() {
        return batchCountViaGetUpdateCount;
    }

    public final boolean isUseQuotes() {
        return useQuotes;
    }

    public final boolean isUnionsWrapped() {
        return unionsWrapped;
    }

    public final boolean isFunctionJoinsWrapped() {
        return functionJoinsWrapped;
    }

    public final boolean isLimitRequired() {
        return limitRequired;
    }

    public final String getNullsFirst() {
        return nullsFirst;
    }

    public final String getNullsLast() {
        return nullsLast;
    }

    public boolean isCountViaAnalytics() {
        return countViaAnalytics;
    }

    public boolean isWrapSelectParameters() {
        return wrapSelectParameters;
    }

    public boolean isArraysSupported() {
        return arraysSupported;
    }

    public int getListMaxSize() {
        return listMaxSize;
    }

    public boolean isSupportsUnquotedReservedWordsAsIdentifier() {
        return supportsUnquotedReservedWordsAsIdentifier;
    }

    protected void newLineToSingleSpace() {
        for (Class<?> cl : Arrays.<Class<?>>asList(getClass(), SQLTemplates.class)) {
            for (Field field : cl.getDeclaredFields()) {
                try {
                    if (field.getType().equals(String.class)) {
                        field.setAccessible(true);
                        Object val = field.get(this);
                        if (val != null) {
                            field.set(this, val.toString().replace('\n',' '));
                        }

                    }
                } catch (IllegalAccessException e) {
                    throw new QueryException(e.getMessage(), e);
                }
            }
        }
    }

    public final String quoteIdentifier(String identifier) {
        return quoteIdentifier(identifier, false);
    }

    public final String quoteIdentifier(String identifier, boolean precededByDot) {
        if (useQuotes || requiresQuotes(identifier, precededByDot)) {
            return quoteStr + identifier + quoteStr;
        } else {
            return identifier;
        }
    }

    protected boolean requiresQuotes(final String identifier, final boolean precededByDot) {
        if (NON_UNDERSCORE_ALPHA_NUMERIC.matchesAnyOf(identifier)) {
            return true;
        } else if (precededByDot && supportsUnquotedReservedWordsAsIdentifier) {
            return false;
        } else {
            return isReservedWord(identifier);
        }
    }

    private boolean isReservedWord(String identifier) {
        return reservedWords.contains(identifier.toUpperCase());
    }

    /**
     * template method for SELECT serialization
     *
     * @param metadata
     * @param forCountRow
     * @param context
     */
    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        context.serializeForQuery(metadata, forCountRow);

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    /**
     * template method for DELETE serialization
     *
     * @param metadata
     * @param entity
     * @param context
     */
    public void serializeDelete(QueryMetadata metadata, RelationalPath<?> entity, SQLSerializer context) {
        context.serializeForDelete(metadata, entity);

        // limit
        if (metadata.getModifiers().isRestricting()) {
            serializeModifiers(metadata, context);
        }

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    /**
     * template method for INSERT serialization
     *
     * @param metadata
     * @param entity
     * @param columns
     * @param values
     * @param subQuery
     * @param context
     */
    public void serializeInsert(QueryMetadata metadata, RelationalPath<?> entity,
            List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery,
            SQLSerializer context) {
        context.serializeForInsert(metadata, entity, columns, values, subQuery);

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    /**
     * template method for MERGE serialization
     *
     * @param metadata
     * @param entity
     * @param keys
     * @param columns
     * @param values
     * @param subQuery
     * @param context
     */
    public void serializeMerge(QueryMetadata metadata, RelationalPath<?> entity,
            List<Path<?>> keys, List<Path<?>> columns, List<Expression<?>> values,
            SubQueryExpression<?> subQuery, SQLSerializer context) {
        context.serializeForMerge(metadata, entity, keys, columns, values, subQuery);

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    /**
     * template method for UPDATE serialization
     *
     * @param metadata
     * @param entity
     * @param updates
     * @param context
     */
    public void serializeUpdate(QueryMetadata metadata, RelationalPath<?> entity,
            List<Pair<Path<?>, Expression<?>>> updates, SQLSerializer context) {
        context.serializeForUpdate(metadata, entity, updates);

        // limit
        if (metadata.getModifiers().isRestricting()) {
            serializeModifiers(metadata, context);
        }

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    /**
     * template method for LIMIT and OFFSET serialization
     *
     * @param metadata
     * @param context
     */
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.getLimit() != null) {
            context.handle(limitTemplate, mod.getLimit());
        } else if (limitRequired) {
            context.handle(limitTemplate, Integer.MAX_VALUE);
        }
        if (mod.getOffset() != null) {
            context.handle(offsetTemplate, mod.getOffset());
        }
    }

    protected void addCustomType(Type<?> type) {
        customTypes.add(type);
    }

    protected void setAsc(String asc) {
        this.asc = asc;
    }

    protected void setAutoIncrement(String autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    protected void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    protected void setCount(String count) {
        this.count = count;
    }

    protected void setCountStar(String countStar) {
        this.countStar = countStar;
    }

    protected void setDelete(String delete) {
        this.delete = delete;
    }

    protected void setDesc(String desc) {
        this.desc = desc;
    }

    protected void setDistinctCountEnd(String distinctCountEnd) {
        this.distinctCountEnd = distinctCountEnd;
    }

    protected void setDistinctCountStart(String distinctCountStart) {
        this.distinctCountStart = distinctCountStart;
    }

    protected void setDummyTable(String dummyTable) {
        this.dummyTable = dummyTable;
    }

    protected void setFrom(String from) {
        this.from = from;
    }

    protected void setFullJoin(String fullJoin) {
        this.fullJoin = fullJoin;
    }

    protected void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    protected void setHaving(String having) {
        this.having = having;
    }

    protected void setInnerJoin(String innerJoin) {
        this.innerJoin = innerJoin;
    }

    protected void setInsertInto(String insertInto) {
        this.insertInto = insertInto;
    }

    protected void setJoin(String join) {
        this.join = join;
    }

    protected void setKey(String key) {
        this.key = key;
    }

    protected void setLeftJoin(String leftJoin) {
        this.leftJoin = leftJoin;
    }

    protected void setRightJoin(String rightJoin) {
        this.rightJoin = rightJoin;
    }

    protected void setMergeInto(String mergeInto) {
        this.mergeInto = mergeInto;
    }

    protected void setNativeMerge(boolean nativeMerge) {
        this.nativeMerge = nativeMerge;
    }

    protected void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    protected void setOffsetTemplate(String offsetTemplate) {
        this.offsetTemplate = offsetTemplate;
    }

    protected void setOn(String on) {
        this.on = on;
    }

    protected void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    protected void setSelect(String select) {
        this.select = select;
    }

    protected void setSelectDistinct(String selectDistinct) {
        this.selectDistinct = selectDistinct;
    }

    protected void setSet(String set) {
        this.set = set;
    }

    protected void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    protected void setUpdate(String update) {
        this.update = update;
    }

    protected void setValues(String values) {
        this.values = values;
    }

    protected void setDefaultValues(String defaultValues) {
        this.defaultValues = defaultValues;
    }

    protected void setWhere(String where) {
        this.where = where;
    }

    protected void setWith(String with) {
        this.with = with;
    }

    protected void setWithRecursive(String withRecursive) {
        this.withRecursive = withRecursive;
    }

    protected void setCreateIndex(String createIndex) {
        this.createIndex = createIndex;
    }

    protected void setCreateUniqueIndex(String createUniqueIndex) {
        this.createUniqueIndex = createUniqueIndex;
    }

    protected void setCreateTable(String createTable) {
        this.createTable = createTable;
    }

    protected void setPrintSchema(boolean printSchema) {
        this.printSchema = printSchema;
    }

    protected void setParameterMetadataAvailable(boolean parameterMetadataAvailable) {
        this.parameterMetadataAvailable = parameterMetadataAvailable;
    }

    protected void setBatchCountViaGetUpdateCount(boolean batchCountViaGetUpdateCount) {
        this.batchCountViaGetUpdateCount = batchCountViaGetUpdateCount;
    }

    protected void setUnionsWrapped(boolean unionsWrapped) {
        this.unionsWrapped = unionsWrapped;
    }

    protected void setFunctionJoinsWrapped(boolean functionJoinsWrapped) {
        this.functionJoinsWrapped = functionJoinsWrapped;
    }

    protected void setNullsFirst(String nullsFirst) {
        this.nullsFirst = nullsFirst;
    }

    protected void setNullsLast(String nullsLast) {
        this.nullsLast = nullsLast;
    }

    protected void setLimitRequired(boolean limitRequired) {
        this.limitRequired = limitRequired;
    }

    protected void setCountDistinctMultipleColumns(boolean countDistinctMultipleColumns) {
        this.countDistinctMultipleColumns = countDistinctMultipleColumns;
    }

    protected void setCountViaAnalytics(boolean countViaAnalytics) {
        this.countViaAnalytics = countViaAnalytics;
    }

    protected void setWrapSelectParameters(boolean b) {
        this.wrapSelectParameters = b;
    }

    protected void setArraysSupported(boolean b) {
        this.arraysSupported = b;
    }

    protected void setListMaxSize(int i ) {
        listMaxSize = i;
    }

    public void setSupportsUnquotedReservedWordsAsIdentifier(boolean b) {
        this.supportsUnquotedReservedWordsAsIdentifier = b;
    }

}
