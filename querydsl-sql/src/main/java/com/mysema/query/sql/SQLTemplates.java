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
package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.primitives.Primitives;
import com.mysema.query.JoinType;
import com.mysema.query.QueryException;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.TemplateExpressionImpl;
import com.mysema.query.types.Templates;

/**
 * SQLTemplates extends Templates to provides SQL specific extensions
 * and acts as database specific Dialect for Querydsl SQL
 *
 * @author tiwe
 */
public class SQLTemplates extends Templates {

    enum DateTimeType {DATE, TIME, DATETIME};

    public static final Expression<?> RECURSIVE = TemplateExpressionImpl.create(Object.class, "");

    public static final SQLTemplates DEFAULT = new SQLTemplates("\"",'\\',false);

    public static abstract class Builder {

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

    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

    private final Map<Class<?>, String> class2type = new HashMap<Class<?>, String>();

    private final String quoteStr;

    private final boolean useQuotes;

    private boolean printSchema;

    private String createTable = "create table ";

    private String asc = " asc";

    private String autoIncrement = " auto_increment";

    private String columnAlias = " ";

    private String count = "count ";

    private String countStar = "count(*)";

    private String deleteFrom = "delete from ";

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

    private String where = "\nwhere ";

    private String with = "with ";

    private String withRecursive = "with recursive ";

    private String createIndex = "create index ";

    private String createUniqueIndex = "create unique index ";

    private String nullsFirst = " nulls first";

    private String nullsLast = " nulls last";

    private boolean parameterMetadataAvailable = true;

    private boolean batchCountViaGetUpdateCount = false;

    private boolean bigDecimalSupported = true;

    private boolean unionsWrapped = true;

    private boolean functionJoinsWrapped = false;

    protected SQLTemplates(String quoteStr, char escape, boolean useQuotes) {
        super(escape);
        this.quoteStr = quoteStr;
        this.useQuotes = useQuotes;

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

        add(Ops.DateTimeOps.YEAR_MONTH, "(year({0}) * 100 + month({0}))");
        add(Ops.DateTimeOps.YEAR_WEEK, "(year({0}) * 100 + week({0}))");

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
        add(SQLOps.UNION, "{0}\nunion\n{1}", 1);
        add(SQLOps.UNION_ALL, "{0}\nunion all\n{1}", 1);
        add(SQLOps.NEXTVAL, "nextval('{0s}')");

        add(SQLOps.ROWNUMBER, "row_number()");
        add(SQLOps.RANK, "rank()");
        add(SQLOps.DENSERANK, "dense_rank()");
        add(SQLOps.FIRSTVALUE, "first_value({0})");
        add(SQLOps.LASTVALUE, "last_value({0})");
        add(SQLOps.LEAD, "lead({0})");
        add(SQLOps.LAG, "lag({0})");

        add(Ops.AggOps.BOOLEAN_ANY, "some({0})");
        add(Ops.AggOps.BOOLEAN_ALL, "every({0})");

        for (Class<?> cl : new Class[] { Boolean.class, Byte.class,
                Double.class, Float.class, Integer.class, Long.class,
                Short.class, String.class }) {
            class2type.put(cl, cl.getSimpleName().toLowerCase(Locale.ENGLISH));
        }

        class2type.put(Boolean.class, "bit");
        class2type.put(Byte.class, "tinyint");
        class2type.put(Long.class, "bigint");
        class2type.put(Short.class, "smallint");
        class2type.put(String.class, "varchar");
        class2type.put(java.sql.Date.class, "date");
        class2type.put(java.sql.Time.class, "time");
        class2type.put(java.sql.Timestamp.class, "timestamp");
    }

    public String asLiteral(Object o) {
        if (o instanceof Character) {
            return "'" + escapeLiteral(o.toString()) + "'";
        } else if (o instanceof String) {
            return "'" + escapeLiteral(o.toString()) + "'";
        // java.util.Date
        } else if (o instanceof java.util.Date) {
            java.util.Date date = (java.util.Date)o;
            if (o instanceof java.sql.Date) {
                return asLiteral(DateTimeType.DATE, dateFormatter.print(date.getTime()));
            } else if (o instanceof java.sql.Time) {
                return asLiteral(DateTimeType.TIME, timeFormatter.print(date.getTime()));
            } else {
                return asLiteral(DateTimeType.DATETIME, dateTimeFormatter.print(date.getTime()));
            }
        // Joda time
        } else if (o instanceof ReadablePartial) {
            ReadablePartial partial = (ReadablePartial)o;
            if (o instanceof LocalDate) {
                return asLiteral(DateTimeType.DATE, dateFormatter.print(partial));
            } else if (o instanceof LocalTime) {
                return asLiteral(DateTimeType.TIME, timeFormatter.print(partial));
            } else {
                return asLiteral(DateTimeType.DATETIME, dateTimeFormatter.print(partial));
            }
        } else if (o instanceof ReadableInstant) {
            return asLiteral(DateTimeType.DATETIME, dateTimeFormatter.print((ReadableInstant)o));
        } else {
            return o.toString();
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
            } else if (ch == '\\') {
                builder.append("\\");
            }
            builder.append(ch);
        }
        return builder.toString();
    }

    public String asLiteral(DateTimeType type, String literal) {
        // SQL 92 standard
        String keyword = "timestamp";
        if (type == DateTimeType.DATE) {
            keyword = "date";
        } else if (type == DateTimeType.TIME) {
            keyword = "time";
        }
        return "(" + keyword + " '" + literal + "')";
    }

    protected void addClass2TypeMappings(String type, Class<?>... classes) {
        for (Class<?> cl : classes) {
            class2type.put(cl, type);
        }
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

    public final String getDeleteFrom() {
        return deleteFrom;
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

    public String getTypeForCast(Class<?> cl) {
        return getTypeForClass(cl);
    }

    public String getTypeForClass(Class<?> cl) {
        Class<?> clazz = Primitives.wrap(cl);
        if (class2type.containsKey(clazz)) {
            return class2type.get(clazz);
        } else {
            throw new IllegalArgumentException("Got not type for " + clazz.getName());
        }
    }

    public final String getUpdate() {
        return update;
    }

    public final String getValues() {
        return values;
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

    public final boolean isPrintSchema() {
        return printSchema;
    }

    public final String getWith() {
        return with;
    }

    public final String getWithRecursive() {
        return withRecursive;
    }

    public final boolean isParameterMetadataAvailable() {
        return parameterMetadataAvailable;
    }

    public final boolean isBatchCountViaGetUpdateCount() {
        return batchCountViaGetUpdateCount;
    }

    public final boolean isBigDecimalSupported() {
        return bigDecimalSupported;
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

    public final String getNullsFirst() {
        return nullsFirst;
    }

    public final String getNullsLast() {
        return nullsLast;
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
        if (useQuotes || requiresQuotes(identifier)) {
            return quoteStr + identifier + quoteStr;
        } else {
            return identifier;
        }
    }

    protected boolean requiresQuotes(final String identifier) {
        for (int i = 0; i < identifier.length(); i++) {
            final char ch = identifier.charAt(i);
            //0-9,a-z,A-Z_
            if (ch < '0' || (ch > '9' && ch < 'A') || (ch > 'Z' && ch < '_') || ch > 'z') {
                return true;
            }
        }
        return false;
    }

    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        context.serializeForQuery(metadata, forCountRow);

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.getLimit() != null) {
            context.handle(limitTemplate, mod.getLimit());
        }
        if (mod.getOffset() != null) {
            context.handle(offsetTemplate, mod.getOffset());
        }
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

    protected void setDeleteFrom(String deleteFrom) {
        this.deleteFrom = deleteFrom;
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

    protected void setBigDecimalSupported(boolean bigDecimalSupported) {
        this.bigDecimalSupported = bigDecimalSupported;
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

}
