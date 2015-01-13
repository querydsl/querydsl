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
package com.querydsl.sql;

import java.sql.Types;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;

/**
 * DB2Templates is an SQL dialect for DB2 10.1.2
 *
 * @author tiwe
 *
 */
public class DB2Templates extends SQLTemplates {

    protected static final Set<String> DB2_RESERVED_WORDS = ImmutableSet.of(
            "ACCORDING", "ACCTNG", "ACTION", "ACTIVATE", "ADD", "AFTER",
            "ALIAS","ALL","ALLOCATE","ALLOW","ALTER","AND","ANY","APPEND",
            "APPLNAME","ARRAY","ARRAY_AGG","AS","ASC","ASENSITIVE","ASSOCIATE",
            "ASUTIME","AT","ATOMIC","ATTRIBUTES","AUDIT","AUTHORIZATION","AUX",
            "AUXILIARY","BEFORE","BEGIN","BETWEEN","BINARY","BIND","BIT",
            "BUFFERPOOL","BY","CACHE","CALL","CALLED","CAPTURE","CARDINALITY",
            "CASCADED","CASE","CAST","CCSID","CHAR","CHARACTER","CHECK","CL",
            "CLIENT","CLONE","CLOSE","CLUSTER","COLLECT","COLLECTION","COLLID",
            "COLUMN","COMMENT","COMMIT","COMPACT","COMPRESS","CONCAT","CONCURRENT",
            "CONDITION","CONNECT","CONNECT_BY_ROOT","CONNECTION","CONSTRAINT",
            "CONTAINS","CONTENT","CONTINUE","COPY","COUNT","COUNT_BIG","CREATE",
            "CROSS","CUBE","CURRENT","CURRENT_DATE","CURRENT_LC_CTYPE",
            "CURRENT_PATH","CURRENT_SCHEMA","CURRENT_SERVER","CURRENT_TIME",
            "CURRENT_TIMESTAMP","CURRENT_TIMEZONE","CURRENT_USER","CURSOR",
            "CYCLE","DATA","DATABASE","DATAPARTITIONNAME","DATAPARTITIONNUM",
            "DATE","DAY","DAYS","DB2GENERAL","DB2GENRL","DB2SQL","DBINFO",
            "DBPARTITIONNAME","DBPARTITIONNUM","DEACTIVATE","DEALLOCATE","DECLARE",
            "DEFAULT","DEFAULTS","DEFER","DEFINE","DEFINITION","DELETE","DENSERANK",
            "DENSE_RANK","DESC","DESCRIBE","DESCRIPTOR","DETERMINISTIC","DIAGNOSTICS",
            "DISABLE","DISALLOW","DISCONNECT","DISTINCT","DO","DOCUMENT","DOUBLE",
            "DROP","DSSIZE","DYNAMIC","EACH","EDITPROC","ELSE","ELSEIF","ENABLE",
            "ENCODING","ENCRYPTION","END","ENDING","END","ENFORCED","ERASE","ESCAPE",
            "EVERY","EXCEPT","EXCEPTION","EXCLUDING","EXCLUSIVE","EXECUTE","EXISTS",
            "EXIT","EXPLAIN","EXTEND","EXTERNAL","EXTRACT","FENCED","FETCH",
            "FIELDPROC","FILE","FINAL","FOR","FOREIGN","FREE","FREEPAGE","FROM",
            "FULL","FUNCTION","GBPCACHE","GENERAL","GENERATED","GET","GLOBAL","GO",
            "GOTO","GRANT","GRAPHIC","GROUP","HANDLER","HASH","HASHED_VALUE","HAVING",
            "HINT","HOLD","HOUR","HOURS","ID","IDENTITY","IF","IGNORE","IMMEDIATE",
            "IMPLICITLY","IN","INCLUDE","INCLUDING","INCLUSIVE","INCREMENT","INDEX",
            "INDEXBP","INDICATOR","INF","INFINITY","INHERIT","INNER","INOUT",
            "INSENSITIVE","INSERT","INTEGRITY","INTERSECT","INTO","IS","ISOBID",
            "ISOLATION","ITERATE","JAR","JAVA","JOIN","KEEP","KEY","LABEL","LANGUAGE",
            "LATERAL","LC_CTYPE","LEAVE","LEFT","LEVEL2","LIKE","LINKTYPE","LOCAL",
            "LOCALDATE","LOCALE","LOCALTIME","LOCALTIMESTAMP","LOCATION","LOCATOR",
            "LOCATORS","LOCK","LOCKSIZE","LOG","LOGGED","LONG","LOOP","MAINTAINED",
            "MATCHED","MATERIALIZED","MAXVALUE","MERGE","MICROSECOND","MICROSECONDS",
            "MINPCTUSED","MINUTE","MINUTES","MINVALUE","MIXED","MODE","MODIFIES",
            "MONTH","MONTHS","NAMESPACE","NAN","NATIONAL","NCHAR","NCLOB","NEW",
            "NEW_TABLE","NEXTVAL","NO","NOCACHE","NOCYCLE","NODENAME","NODENUMBER",
            "NOMAXVALUE","NOMINVALUE","NONE","NOORDER","NORMALIZED","NOT","NULL",
            "NULLS","NUMPARTS","NVARCHAR","OBID","OF","OLD","OLD_TABLE","ON","OPEN",
            "OPTIMIZATION","OPTIMIZE","OPTION","OR","ORDER","ORDINALITY","ORGANIZE",
            "OUT","OUTER","OVER","OVERRIDING","PACKAGE","PADDED","PAGE","PAGESIZE",
            "PARAMETER","PART","PARTITION","PARTITIONED","PARTITIONING","PARTITIONS",
            "PASSWORD","PATH","PCTFREE","PIECESIZE","PLAN","POSITION","PRECISION",
            "PREPARE","PREVVAL","PRIMARY","PRIQTY","PRIVILEGES","PROCEDURE","PROGRAM",
            "PROGRAMID","PSID","PUBLIC","QUERY","QUERYNO","RANGE","RANK","RCDFMT",
            "READ","READS","RECOVERY","REFERENCES","REFERENCING","REFRESH","RELEASE",
            "RENAME","REPEAT","RESET","RESIGNAL","RESTART","RESTRICT","RESULT",
            "RESULT_SET_LOCATOR","RETURN","RETURNS","REVOKE","RID","RIGHT","ROLE",
            "ROLLBACK","ROLLUP","ROUND_CEILING","ROUND_DOWN","ROUND_FLOOR","ROUND_HALF_DOWN",
            "ROUND_HALF_EVEN","ROUND_HALF_UP","ROUND_UP","ROUTINE","ROW","ROWNUMBER",
            "ROWS","ROWSET","ROW_NUMBER","RRN","RUN","SAVEPOINT","SBCS","SCHEMA",
            "SCRATCHPAD","SCROLL","SEARCH","SECOND","SECONDS","SECQTY","SECURITY",
            "SELECT","SENSITIVE","SEQUENCE","SESSION","SESSION_USER","SET","SIGNAL",
            "SIMPLE","SKIP","SNAN","SOME","SOURCE","SPECIFIC","SQL","SQLID","STACKED",
            "STANDARD","START","STARTING","STATEMENT","STATIC","STATMENT","STAY","STOGROUP",
            "STORES","STYLE","SUBSTRING","SUMMARY","SYNONYM","SYSFUN","SYSIBM","SYSPROC",
            "SYSTEM","SYSTEM_USER","TABLE","TABLESPACE","TABLESPACES","THEN","THREADSAFE",
            "TIME","TIMESTAMP","TO","TRANSACTION","TRIGGER","TRIM","TRIM_ARRAY","TRUNCATE",
            "TYPE","UNDO","UNION","UNIQUE","UNIT","UNNEST","UNTIL","UPDATE","URI","USAGE",
            "USE","USER","USERID","USING","VALIDPROC","VALUE","VALUES","VARIABLE","VARIANT",
            "VCAT","VERSION","VIEW","VOLATILE","VOLUMES","WAIT","WHEN","WHENEVER","WHERE",
            "WHILE","WITH","WITHOUT","WLM","WRAPPED","WRITE","WRKSTNNAME","XMLAGG",
            "XMLATTRIBUTES","XMLCAST","XMLCOMMENT","XMLCONCAT","XMLDOCUMENT","XMLELEMENT",
            "XMLEXISTS","XMLFOREST","XMLGROUP","XMLNAMESPACES","XMLPARSE","XMLPI",
            "XMLROW","XMLSERIALIZE","XMLTABLE","XMLTEXT","XMLVALIDATE","XSLTRANSFORM",
            "XSROBJECT","YEAR","YEARS","YES");

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final DB2Templates DEFAULT = new DB2Templates();

    private String limitTemplate = "\nfetch first {0s} rows only";

    private String outerQueryStart = "select * from (\n  ";

    private String outerQueryEnd = ") a where ";

    private String limitOffsetTemplate = "rn > {0} and rn <= {1}";

    private String offsetTemplate = "rn > {0}";

    private String outerQuerySuffix = " order by rn";

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new DB2Templates(escape, quote);
            }
        };
    }

    public DB2Templates() {
        this('\\',false);
    }

    public DB2Templates(boolean quote) {
        this('\\',quote);
    }

    public DB2Templates(char escape, boolean quote) {
        super(DB2_RESERVED_WORDS, "\"", escape, quote);
        setDummyTable("sysibm.sysdummy1");
        setAutoIncrement(" generated always as identity");
        setFunctionJoinsWrapped(true);
        setDefaultValues("\nvalues (default)");
        setNullsFirst(null);
        setNullsLast(null);

        add(SQLOps.NEXTVAL, "next value for {0s}");

        add(Ops.MathOps.RANDOM, "rand()");
        add(Ops.MathOps.LN, "log({0})");
        add(Ops.MathOps.LOG, "(log({0}) / log({1}))");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");

        // overrides of the SQL standard functions
        add(Ops.DateTimeOps.SECOND, "second({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.WEEK, "week({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");
        add(Ops.DateTimeOps.YEAR, "year({0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "(year({0}) * 100 + month({0}))");
        add(Ops.DateTimeOps.YEAR_WEEK, "(year({0}) * 100 + week({0}))");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "dayofweek({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "day({0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "dayofyear({0})");

        add(Ops.DateTimeOps.ADD_YEARS, "{0} + {1} years");
        add(Ops.DateTimeOps.ADD_MONTHS, "{0} + {1} months");
        add(Ops.DateTimeOps.ADD_WEEKS, "{0} + {1} weeks");
        add(Ops.DateTimeOps.ADD_DAYS, "{0} + {1} days");
        add(Ops.DateTimeOps.ADD_HOURS, "{0} + {1} hours");
        add(Ops.DateTimeOps.ADD_MINUTES, "{0} + {1} minutes");
        add(Ops.DateTimeOps.ADD_SECONDS, "{0} + {1} seconds");

        // FIXME
        add(Ops.DateTimeOps.DIFF_YEARS, "timestampdiff(256, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_MONTHS, "timestampdiff(64, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_WEEKS, "timestampdiff(32, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_DAYS, "timestampdiff(16, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_HOURS, "timestampdiff(8, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_MINUTES, "timestampdiff(4, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_SECONDS, "timestampdiff(2, char({0} - {1}))");

        add(Ops.DateTimeOps.TRUNC_YEAR, "trunc_timestamp({0}, 'year')");
        add(Ops.DateTimeOps.TRUNC_MONTH, "trunc_timestamp({0}, 'month')");
        add(Ops.DateTimeOps.TRUNC_WEEK, "trunc_timestamp({0}, 'week')");
        add(Ops.DateTimeOps.TRUNC_DAY, "trunc_timestamp({0}, 'day')");
        add(Ops.DateTimeOps.TRUNC_HOUR, "trunc_timestamp({0}, 'hour')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "trunc_timestamp({0}, 'minute')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "trunc_timestamp({0}, 'second')");

        addTypeNameToCode("smallint", Types.BOOLEAN, true);
        addTypeNameToCode("smallint", Types.TINYINT, true);
        addTypeNameToCode("long varchar for bit data", Types.LONGVARBINARY);
        addTypeNameToCode("varchar () for bit data", Types.VARBINARY);
        addTypeNameToCode("char () for bit data", Types.BINARY);
        addTypeNameToCode("long varchar", Types.LONGVARCHAR, true);
        addTypeNameToCode("object", Types.JAVA_OBJECT, true);
        addTypeNameToCode("xml", Types.SQLXML,true);
    }

    @Override
    public String getCastTypeNameForCode(int code) {
        switch (code) {
            case Types.VARCHAR:  return "varchar(4000)";
            default: return super.getCastTypeNameForCode(code);
        }
    }


    @Override
    public String serialize(String literal, int jdbcType) {
        if (jdbcType == Types.TIMESTAMP) {
            return "{ts '" + literal + "'}";
        } else if (jdbcType == Types.DATE) {
            return "{d '" + literal + "'}";
        } else if (jdbcType == Types.TIME) {
            return "{t '" + literal + "'}";
        } else {
            return super.serialize(literal, jdbcType);
        }
    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        if (!forCountRow && metadata.getModifiers().isRestricting() && !metadata.getJoins().isEmpty()) {
            QueryModifiers mod = metadata.getModifiers();
            if (mod.getOffset() == null) {
                context.serializeForQuery(metadata, forCountRow);
                context.handle(limitTemplate, mod.getLimit());
            } else {
                context.append(outerQueryStart);
                metadata = metadata.clone();
                WindowFunction<Long> rn = SQLExpressions.rowNumber().over();
                for (OrderSpecifier<?> os : metadata.getOrderBy()) {
                    rn.orderBy(os);
                }
                metadata.addProjection(rn.as("rn"));
                metadata.clearOrderBy();
                context.serializeForQuery(metadata, forCountRow);
                context.append(outerQueryEnd);
                if (mod.getLimit() == null) {
                    context.handle(offsetTemplate, mod.getOffset());
                } else {
                    context.handle(limitOffsetTemplate, mod.getOffset(), mod.getLimit() + mod.getOffset());
                }
                context.append(outerQuerySuffix);
            }

        } else {
            context.serializeForQuery(metadata, forCountRow);
        }

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(QueryFlag.Position.END, metadata.getFlags());
        }
    }

    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        // do nothing
    }


}
