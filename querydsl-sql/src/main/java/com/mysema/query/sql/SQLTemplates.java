/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.JoinType;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.PathType;
import com.mysema.query.types.Templates;
import com.mysema.query.types.operation.Ops;

/**
 * SQLTemplates extended Templates to provided SQL specific extensions
 * and acts as database specific Dialect for Querydsl SQL
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLTemplates extends Templates {
    
    public static final SQLTemplates DEFAULT = new SQLTemplates();
    
    private String asc = " asc";
    
    private final Map<Class<?>, String> class2type = new HashMap<Class<?>, String>();

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
    
    private String leftJoin = "\nleft join ";
    
    private String limitTemplate = "\nlimit {0}";
    
    private String offsetTemplate = "\noffset {0}";
    
    private String on = "\non ";
    
    private String orderBy = "\norder by ";
    
    @Nullable
    private String quoteStr;
    
    private String select = "select ";
    
    private String selectDistinct = "select distinct ";
    
    private String tableAlias = " ";

    private String union = "\nunion\n";
    
    private String update = "update ";

    private String values = "\nvalues ";
    
    private String where = "\nwhere ";
    
    protected SQLTemplates(){
        this(null);
    }
    
    protected SQLTemplates(@Nullable String quoteStr) {
        this.quoteStr = quoteStr;
        
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

        // date time
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_timestamp");
        add(Ops.DateTimeOps.MILLISECOND, "0");
        add(Ops.DateTimeOps.YEAR_MONTH, "year({0}) * 100 + month({0})");

        // string
        add(Ops.CHAR_AT, "cast(substr({0},{1}+1,1) as char)");
        add(Ops.ENDS_WITH, "{0} like {%1}");
        add(Ops.ENDS_WITH_IC, "{0l} like lower({%1})");
        add(Ops.EQ_IGNORE_CASE, "{0l} = {1l}");
        add(Ops.INDEX_OF, "locate({1},{0})-1");
        add(Ops.INDEX_OF_2ARGS, "locate({1},{0},{2}+1)-1");
        add(Ops.STARTS_WITH, "{0} like {1%}");
        add(Ops.STARTS_WITH_IC, "{0l} like lower({1%})");       
        add(Ops.STRING_CONTAINS, "{0} like {%1%}");
        add(Ops.STRING_CONTAINS_IC, "{0l} like lower({%1%})");
        add(Ops.STRING_IS_EMPTY, "length({0}) = 0");       
        add(Ops.SUBSTR_1ARG, "substr({0},{1}+1)");
        add(Ops.SUBSTR_2ARGS, "substr({0},{1}+1,{2})");
        
        if (quoteStr != null){
            add(PathType.PROPERTY, "{0}." + quoteStr + "{1s}" + quoteStr);
            add(PathType.VARIABLE, quoteStr + "{0s}" + quoteStr);            
        }
        
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
    }

    public void addClass2TypeMappings(String type, Class<?>... classes) {
        for (Class<?> cl : classes) {
            class2type.put(cl, type);
        }
    }
    
    public String getAsc() {
        return asc;
    }
    
    public Map<Class<?>, String> getClass2type() {
        return class2type;
    }
    
    public Map<Class<?>, String> getClass2Type() {
        return class2type;
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

    public String getJoinSymbol(JoinType joinType){
        switch (joinType) {
            case FULLJOIN:  return fullJoin;
            case INNERJOIN: return innerJoin;
            case JOIN:      return join;
            case LEFTJOIN:  return leftJoin;
        }
        return ", ";
    }

    public String getLeftJoin() {
        return leftJoin;
    }

    public String getLimitTemplate() {
        return limitTemplate;
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

    public String getTableAlias() {
        return tableAlias;
    }

    public String getUnion() {
        return union;
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

    public boolean isSupportsAlias() {
        return true;
    }

    public SQLTemplates newLineToSingleSpace() {
        for (Field field : SQLTemplates.class.getDeclaredFields()) {
            try {
                if (field.getType().equals(String.class)) {
                    Object val = field.get(this);
                    if (val != null){
                        field.set(this, val.toString().replace('\n',' '));    
                    }
                    
                }
            } catch (IllegalAccessException e) {
                throw new QueryException(e.getMessage(), e);
            }
        }
        return this;
    }

    public final String quoteColumnName(String column){
        if (quoteStr != null){
            return quoteStr + column + quoteStr;
        }else{
            return column;
        }        
    }

    public final String quoteTableName(String table){
        if (quoteStr != null){
            return quoteStr + table + quoteStr;
        }else{
            return table;    
        }        
    }

    public void serialize(QueryMetadata metadata, boolean forCountRow, SerializationContext context) {
        context.serialize(metadata, forCountRow);
        
        if (!forCountRow && metadata.getModifiers().isRestricting()){
            serializeModifiers(metadata, context);  
        }        
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

    protected void setLeftJoin(String leftJoin) {
        this.leftJoin = leftJoin;
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

    protected void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    protected void setUnion(String union) {
        this.union = union;
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
    
}
