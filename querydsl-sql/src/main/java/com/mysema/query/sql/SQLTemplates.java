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
import java.util.regex.Pattern;

import com.google.common.primitives.Primitives;
import com.mysema.commons.lang.Assert;
import com.mysema.query.JoinType;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Templates;

/**
 * SQLTemplates extends Templates to provides SQL specific extensions
 * and acts as database specific Dialect for Querydsl SQL
 *
 * @author tiwe
 */
public class SQLTemplates extends Templates {
    
    public static final Operator<Object> CAST = new OperatorImpl<Object>("CAST",Object.class, Object.class);
    
    public static final Operator<Object> UNION = new OperatorImpl<Object>("UNION");

    public static final Operator<Object> NEXTVAL = new OperatorImpl<Object>("NEXTVAL", String.class);
    
    public static final SQLTemplates DEFAULT = new SQLTemplates("\"",'\\',false);
    
    private static final Pattern IDENTIFIER_CHARS = Pattern.compile("[a-zA-Z0-9_\\-]+");
    
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

    private String forUpdate = "\nfor update";
    
    private String forShare = "\nfor share";
    
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

    private final String limitTemplate = "\nlimit {0}";

    private String mergeInto = "merge into ";

    private boolean nativeMerge;

    private String notNull = " not null";
    
    private String noWait = " nowait";

    private String offsetTemplate = "\noffset {0}";

    private String on = "\non ";

    private String orderBy = "\norder by ";

    private String select = "select ";

    private String selectDistinct = "select distinct ";

    private String set = "set ";

    private String tableAlias = " ";

    private String union = "\nunion\n";
    
    private String unionAll = "\nunion all\n";

    private String update = "update ";

    private String values = "\nvalues ";
    
    private String where = "\nwhere ";
    
    private String with = "with ";
    
    private String createIndex = "create index ";
    
    private String createUniqueIndex = "create unique index ";
    
    private boolean parameterMetadataAvailable = true;
    
    private boolean batchCountViaGetUpdateCount = false;
    
    private boolean bigDecimalSupported = true;
    
    protected SQLTemplates(String quoteStr, char escape, boolean useQuotes) {
        super(escape);
        this.quoteStr = Assert.notNull(quoteStr, "quoteStr");
        this.useQuotes = useQuotes;
        
        // boolean
        add(Ops.AND, "{0} and {1}", 36);
        add(Ops.NOT, "not {0}", 3);
        add(Ops.OR, "{0} or {1}", 38);
        add(Ops.XNOR, "{0} xnor {1}", 39);
        add(Ops.XOR, "{0} xor {1}", 39);

        // math
        add(Ops.MathOps.RANDOM, "rand()");
        add(Ops.MathOps.CEIL, "ceiling({0})");
        add(Ops.MathOps.POWER, "power({0},{1})");
        add(Ops.MOD, "mod({0},{1})", 0);

        // date time
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_time");
        add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp");
        add(Ops.DateTimeOps.MILLISECOND, "0");
        add(Ops.DateTimeOps.YEAR_MONTH, "year({0}) * 100 + month({0})");

        // string
        add(Ops.CONCAT, "{0} || {1}", 38);
        add(Ops.MATCHES, "{0} regexp {1}", 25);
        add(Ops.CHAR_AT, "cast(substr({0},{1s}+1,1) as char)");
        add(Ops.EQ_IGNORE_CASE, "{0l} = {1l}");
        add(Ops.INDEX_OF, "locate({1},{0})-1");
        add(Ops.INDEX_OF_2ARGS, "locate({1},{0},{2s}+1)-1");
        add(Ops.STRING_IS_EMPTY, "length({0}) = 0");
        add(Ops.SUBSTR_1ARG, "substr({0},{1s}+1)");
        add(Ops.SUBSTR_2ARGS, "substr({0},{1s}+1,{2s}-{1s})");
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

        add(CAST, "cast({0} as {1s})");
        add(UNION, "{0}\nunion\n{1}");
        add(NEXTVAL, "nextval('{0s}')");

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

    protected void addClass2TypeMappings(String type, Class<?>... classes) {
        for (Class<?> cl : classes) {
            class2type.put(cl, type);
        }
    }

    public String getAsc() {
        return asc;
    }

    public String getAutoIncrement() {
        return autoIncrement;
    }

    public String getColumnAlias() {
        return columnAlias;
    }

    public String getCount() {
        return count;
    }

    public String getCountStar() {
        return countStar;
    }

    public String getDeleteFrom() {
        return deleteFrom;
    }

    public String getDesc() {
        return desc;
    }

    public String getDistinctCountEnd() {
        return distinctCountEnd;
    }

    public String getDistinctCountStart() {
        return distinctCountStart;
    }

    public String getDummyTable() {
        return dummyTable;
    }

    public String getFrom() {
        return from;
    }

    public String getFullJoin() {
        return fullJoin;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public String getHaving() {
        return having;
    }

    public String getInnerJoin() {
        return innerJoin;
    }

    public String getInsertInto() {
        return insertInto;
    }

    public String getJoin() {
        return join;
    }

    public String getJoinSymbol(JoinType joinType) {
        switch (joinType) {
            case JOIN:      return join;
            case INNERJOIN: return innerJoin;
            case FULLJOIN:  return fullJoin;
            case LEFTJOIN:  return leftJoin;
            case RIGHTJOIN: return rightJoin;
        }
        return ", ";
    }

    public String getKey() {
        return key;
    }

    public String getLeftJoin() {
        return leftJoin;
    }
    
    public String getRightJoin() {
        return rightJoin;
    }

    public String getLimitTemplate() {
        return limitTemplate;
    }

    public String getMergeInto() {
        return mergeInto;
    }

    public String getNotNull() {
        return notNull;
    }

    public String getOffsetTemplate() {
        return offsetTemplate;
    }

    public String getOn() {
        return on;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getSelect() {
        return select;
    }

    public String getSelectDistinct() {
        return selectDistinct;
    }

    public String getSet() {
        return set;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public String getTypeForCast(Class<?> cl) {
        return getTypeForClass(cl);
    }
    
    public final String getTypeForClass(Class<?> cl) {
        Class<?> clazz = Primitives.wrap(cl);
        if (class2type.containsKey(clazz)) {
            return class2type.get(clazz);
        } else {
            throw new IllegalArgumentException("Got not type for " + clazz.getName());
        }
    }

    public String getUnion() {
        return union;
    }

    public String getUnionAll() {
        return unionAll;
    }
    
    public String getUpdate() {
        return update;
    }

    public String getValues() {
        return values;
    }

    public String getWhere() {
        return where;
    }

    public boolean isNativeMerge() {
        return nativeMerge;
    }

    public boolean isSupportsAlias() {
        return true;
    }
    
    public String getCreateIndex() {
        return createIndex;
    }
    
    public String getCreateUniqueIndex() {
        return createUniqueIndex;
    }
    
    public String getCreateTable() {
        return createTable;
    }

    public boolean isPrintSchema() {
        return printSchema;
    }
    
    public String getWith() {
        return with;
    }
    
    public boolean isParameterMetadataAvailable() {
        return parameterMetadataAvailable;
    }

    public boolean isBatchCountViaGetUpdateCount() {
        return batchCountViaGetUpdateCount;
    }
    
    public boolean isBigDecimalSupported() {
        return bigDecimalSupported;
    }

    public String getForUpdate() {
        return forUpdate;
    }

    public String getForShare() {
        return forShare;
    }

    public boolean isUseQuotes() {
        return useQuotes;
    }
    
    public String getNoWait() {
        return noWait;
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
    
    public String quoteIdentifier(String identifier) {
        if (useQuotes || requiresQuotes(identifier)) {
            return quoteStr + identifier + quoteStr;
        } else {
            return identifier;
        }
    }

    protected boolean requiresQuotes(String identifier) {
        return !IDENTIFIER_CHARS.matcher(identifier).matches();
    }
    
    public void serialize(QueryMetadata metadata, boolean forCountRow, SerializationContext context) {
        context.serialize(metadata, forCountRow);
    }

    protected void serializeModifiers(QueryMetadata metadata, SerializationContext context) {
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

    protected void setUnion(String union) {
        this.union = union;
    }

    protected void setUnionAll(String unionAll) {
        this.unionAll = unionAll;
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
    
    protected void setForUpdate(String forUpdate) {
        this.forUpdate = forUpdate;
    }
   
    protected void setForShare(String forShare) {
        this.forShare = forShare;
    }       
   
    protected void setNoWait(String noWait) {
        this.noWait = noWait;
    }

}
