/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.JoinType;
import com.mysema.query.QueryException;
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
    
    private final Map<Class<?>, String> class2type = new HashMap<Class<?>, String>();
    
    private String asc = " asc";

    private String columnAlias = " ";
    
    private String connectBy = "\nconnect by ";
    
    private String connectByNocyclePrior = "\nconnect by nocycle prior ";
    
    private String connectByPrior = "\nconnect by prior ";
    
    private String count = "count ";
    
    private String countStar = "count(*)";
    
    private String distinctCountStart = "count(distinct ";
    
    private String distinctCountEnd = ")";
    
    private String deleteFrom = "delete from ";
    
    private String desc = " desc";
    
    private String dummyTable = "dual";
    
    private String from = "\nfrom ";
    
    private String fullJoin = "\nfull join ";
    
    private String groupBy = "\ngroup by ";
    
    private String having = "\nhaving ";
    
    private String innerJoin = "\ninner join ";
    
    private String join = "\njoin ";
    
    private String leftJoin = "\nleft join ";
    
    private String limit = "\nlimit ";
    
    private boolean limitAndOffsetSymbols = true;
    
    private String limitOffsetTemplate = "";
    
    private String limitTemplate = "";
    
    private String offset = "\noffset ";
    
    private String offsetTemplate = "";

    private String on = "\non ";
    
    private String orderBy = "\norder by ";
    
    private String orderSiblingsBy = "\norder siblings by ";
    
    private String over = "over";
    
    private String partitionBy = "partition by ";
    
    private String select = "select ";
    
    private String selectDistinct = "select distinct ";
    
    private String startWith = "\nstart with ";

    private String sum = "sum";
    
    private String tableAlias = " ";
    
    private String union = "\nunion\n";

    private String update = "update ";

    private String where = "\nwhere ";

    protected SQLTemplates() {
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
        
        for (Class<?> cl : new Class[] { Boolean.class, Byte.class,
                Double.class, Float.class, Integer.class, Long.class,
                Short.class, String.class }) {
            class2type.put(cl, cl.getSimpleName().toLowerCase());
        }

        class2type.put(Boolean.class, "bit");
        class2type.put(Byte.class, "tinyint");
        class2type.put(Long.class, "bigint");
        class2type.put(Short.class, "smallint");
        class2type.put(String.class, "varchar");
    }

    public final Map<Class<?>, String> getClass2Type() {
        return class2type;
    }
    
    public final void addClass2TypeMappings(String type, Class<?>... classes) {
        for (Class<?> cl : classes) {
            class2type.put(cl, type);
        }
    }
    
    public final SQLTemplates newLineToSingleSpace() {
        for (Field field : SQLTemplates.class.getDeclaredFields()) {
            try {
                if (field.getType().equals(String.class)) {
                    field.set(this, field.get(this).toString().replace('\n',' '));
                }
            } catch (IllegalAccessException e) {
                throw new QueryException(e.getMessage(), e);
            }
        }
        return this;
    }
    
    public final String getLimitOffsetCondition(@Nullable Long limit, @Nullable Long offset) {
        if (offset == null) {
            return String.format(limitTemplate, limit);
        } else if (limit == null) {
            return String.format(offsetTemplate, offset);
        } else {
            return String.format(limitOffsetTemplate, limit, offset, limit + offset);
        }
    }

    public final String getAsc() {
        return asc;
    }

    public final void setAsc(String asc) {
        this.asc = asc;
    }

    public final String getColumnAlias() {
        return columnAlias;
    }

    public final void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public final String getConnectBy() {
        return connectBy;
    }

    public final void setConnectBy(String connectBy) {
        this.connectBy = connectBy;
    }

    public final String getConnectByNocyclePrior() {
        return connectByNocyclePrior;
    }

    public final void setConnectByNocyclePrior(String connectByNocyclePrior) {
        this.connectByNocyclePrior = connectByNocyclePrior;
    }

    public final String getConnectByPrior() {
        return connectByPrior;
    }

    public final void setConnectByPrior(String connectByPrior) {
        this.connectByPrior = connectByPrior;
    }

    public final String getCount() {
        return count;
    }

    public final void setCount(String count) {
        this.count = count;
    }

    public final String getCountStar() {
        return countStar;
    }

    public final void setCountStar(String countStar) {
        this.countStar = countStar;
    }

    public final String getDistinctCountStart() {
        return distinctCountStart;
    }

    public final void setDistinctCountStart(String distinctCountStart) {
        this.distinctCountStart = distinctCountStart;
    }

    public final String getDistinctCountEnd() {
        return distinctCountEnd;
    }

    public final void setDistinctCountEnd(String distinctCountEnd) {
        this.distinctCountEnd = distinctCountEnd;
    }

    public final String getDeleteFrom() {
        return deleteFrom;
    }

    public final void setDeleteFrom(String deleteFrom) {
        this.deleteFrom = deleteFrom;
    }

    public final String getDesc() {
        return desc;
    }

    public final void setDesc(String desc) {
        this.desc = desc;
    }

    public final String getDummyTable() {
        return dummyTable;
    }

    public final void setDummyTable(String dummyTable) {
        this.dummyTable = dummyTable;
    }

    public final String getFrom() {
        return from;
    }

    public final void setFrom(String from) {
        this.from = from;
    }

    public final String getFullJoin() {
        return fullJoin;
    }

    public final void setFullJoin(String fullJoin) {
        this.fullJoin = fullJoin;
    }

    public final String getGroupBy() {
        return groupBy;
    }

    public final void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public final String getHaving() {
        return having;
    }

    public final void setHaving(String having) {
        this.having = having;
    }

    public final String getInnerJoin() {
        return innerJoin;
    }

    public final void setInnerJoin(String innerJoin) {
        this.innerJoin = innerJoin;
    }

    public final String getJoin() {
        return join;
    }

    public final void setJoin(String join) {
        this.join = join;
    }

    public final String getLeftJoin() {
        return leftJoin;
    }

    public final void setLeftJoin(String leftJoin) {
        this.leftJoin = leftJoin;
    }

    public final String getLimit() {
        return limit;
    }

    public final void setLimit(String limit) {
        this.limit = limit;
    }

    public final boolean isLimitAndOffsetSymbols() {
        return limitAndOffsetSymbols;
    }

    public final void setLimitAndOffsetSymbols(boolean limitAndOffsetSymbols) {
        this.limitAndOffsetSymbols = limitAndOffsetSymbols;
    }

    public final String getLimitOffsetTemplate() {
        return limitOffsetTemplate;
    }

    public final void setLimitOffsetTemplate(String limitOffsetTemplate) {
        this.limitOffsetTemplate = limitOffsetTemplate;
    }

    public final String getLimitTemplate() {
        return limitTemplate;
    }

    public final void setLimitTemplate(String limitTemplate) {
        this.limitTemplate = limitTemplate;
    }

    public final String getOffset() {
        return offset;
    }

    public final void setOffset(String offset) {
        this.offset = offset;
    }

    public final String getOffsetTemplate() {
        return offsetTemplate;
    }

    public final void setOffsetTemplate(String offsetTemplate) {
        this.offsetTemplate = offsetTemplate;
    }

    public final String getOn() {
        return on;
    }

    public final void setOn(String on) {
        this.on = on;
    }

    public final String getOrderBy() {
        return orderBy;
    }

    public final void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public final String getOrderSiblingsBy() {
        return orderSiblingsBy;
    }

    public final void setOrderSiblingsBy(String orderSiblingsBy) {
        this.orderSiblingsBy = orderSiblingsBy;
    }

    public final String getOver() {
        return over;
    }

    public final void setOver(String over) {
        this.over = over;
    }

    public final String getPartitionBy() {
        return partitionBy;
    }

    public final void setPartitionBy(String partitionBy) {
        this.partitionBy = partitionBy;
    }

    public final String getSelect() {
        return select;
    }

    public final void setSelect(String select) {
        this.select = select;
    }

    public final String getSelectDistinct() {
        return selectDistinct;
    }

    public final void setSelectDistinct(String selectDistinct) {
        this.selectDistinct = selectDistinct;
    }

    public final String getStartWith() {
        return startWith;
    }

    public final void setStartWith(String startWith) {
        this.startWith = startWith;
    }

    public final String getSum() {
        return sum;
    }

    public final void setSum(String sum) {
        this.sum = sum;
    }

    public final String getTableAlias() {
        return tableAlias;
    }

    public final void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public final String getUnion() {
        return union;
    }

    public final void setUnion(String union) {
        this.union = union;
    }

    public final String getUpdate() {
        return update;
    }

    public final void setUpdate(String update) {
        this.update = update;
    }

    public final String getWhere() {
        return where;
    }

    public final void setWhere(String where) {
        this.where = where;
    }

    public final boolean isSupportsAlias() {
        return true;
    }
    
    public final String getJoinSymbol(JoinType joinType){
        switch (joinType) {
            case FULLJOIN:  return fullJoin;
            case INNERJOIN: return innerJoin;
            case JOIN:      return join;
            case LEFTJOIN:  return leftJoin;
        }
        return ", ";
    }
}
