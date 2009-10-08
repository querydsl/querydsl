/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.Templates;
import com.mysema.query.types.operation.Ops;

/**
 * SQLTemplates extended OperationPatterns to provided SQL specific extensions
 * and acts as database specific Dialect for Querydsl SQL
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLTemplates extends Templates {

    private final Map<Class<?>, String> class2type = new HashMap<Class<?>, String>();
    
    private String asc = " asc";

    private String columnAlias = " ";
    
    private String connectBy = "\nconnect by ";
    
    private String connectByNocyclePrior = "\nconnect by nocycle prior ";
    
    private String connectByPrior = "\nconnect by prior ";
    
    private String count = "count ";
    
    private String countStar = "count(*)";
    
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

    public SQLTemplates() {
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

    public Map<Class<?>, String> getClass2Type() {
        return class2type;
    }
    
    public void addClass2TypeMappings(String type, Class<?>... classes) {
        for (Class<?> cl : classes) {
            class2type.put(cl, type);
        }
    }
    
    public SQLTemplates newLineToSingleSpace() {
        for (Field field : SQLTemplates.class.getDeclaredFields()) {
            try {
                if (field.getType().equals(String.class)) {
                    field.set(this, field.get(this).toString().replace('\n',' '));
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this;
    }
    
    public String getLimitOffsetCondition(Long limit, Long offset) {
        if (offset == null) {
            return String.format(limitTemplate, limit);
        } else if (limit == null) {
            return String.format(offsetTemplate, offset);
        } else {
            return String.format(limitOffsetTemplate, limit, offset, limit + offset);
        }
    }

    public String getAsc() {
        return asc;
    }

    public void setAsc(String asc) {
        this.asc = asc;
    }

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public String getConnectBy() {
        return connectBy;
    }

    public void setConnectBy(String connectBy) {
        this.connectBy = connectBy;
    }

    public String getConnectByNocyclePrior() {
        return connectByNocyclePrior;
    }

    public void setConnectByNocyclePrior(String connectByNocyclePrior) {
        this.connectByNocyclePrior = connectByNocyclePrior;
    }

    public String getConnectByPrior() {
        return connectByPrior;
    }

    public void setConnectByPrior(String connectByPrior) {
        this.connectByPrior = connectByPrior;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCountStar() {
        return countStar;
    }

    public void setCountStar(String countStar) {
        this.countStar = countStar;
    }

    public String getDeleteFrom() {
        return deleteFrom;
    }

    public void setDeleteFrom(String deleteFrom) {
        this.deleteFrom = deleteFrom;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDummyTable() {
        return dummyTable;
    }

    public void setDummyTable(String dummyTable) {
        this.dummyTable = dummyTable;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFullJoin() {
        return fullJoin;
    }

    public void setFullJoin(String fullJoin) {
        this.fullJoin = fullJoin;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getInnerJoin() {
        return innerJoin;
    }

    public void setInnerJoin(String innerJoin) {
        this.innerJoin = innerJoin;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getLeftJoin() {
        return leftJoin;
    }

    public void setLeftJoin(String leftJoin) {
        this.leftJoin = leftJoin;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public boolean isLimitAndOffsetSymbols() {
        return limitAndOffsetSymbols;
    }

    public void setLimitAndOffsetSymbols(boolean limitAndOffsetSymbols) {
        this.limitAndOffsetSymbols = limitAndOffsetSymbols;
    }

    public String getLimitOffsetTemplate() {
        return limitOffsetTemplate;
    }

    public void setLimitOffsetTemplate(String limitOffsetTemplate) {
        this.limitOffsetTemplate = limitOffsetTemplate;
    }

    public String getLimitTemplate() {
        return limitTemplate;
    }

    public void setLimitTemplate(String limitTemplate) {
        this.limitTemplate = limitTemplate;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getOffsetTemplate() {
        return offsetTemplate;
    }

    public void setOffsetTemplate(String offsetTemplate) {
        this.offsetTemplate = offsetTemplate;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderSiblingsBy() {
        return orderSiblingsBy;
    }

    public void setOrderSiblingsBy(String orderSiblingsBy) {
        this.orderSiblingsBy = orderSiblingsBy;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getPartitionBy() {
        return partitionBy;
    }

    public void setPartitionBy(String partitionBy) {
        this.partitionBy = partitionBy;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getSelectDistinct() {
        return selectDistinct;
    }

    public void setSelectDistinct(String selectDistinct) {
        this.selectDistinct = selectDistinct;
    }

    public String getStartWith() {
        return startWith;
    }

    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public boolean isSupportsAlias() {
        return true;
    }
}
