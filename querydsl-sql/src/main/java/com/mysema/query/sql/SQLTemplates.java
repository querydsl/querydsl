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
 * SqlOps extended OperationPatterns to provided SQL specific extensions and
 * acts as database specific Dialect for Querydsl SQL
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLTemplates extends Templates {

    private String count = "count ", 
        countStar = "count(*)",
        dummyTable = "dual", 
        select = "select ",
        selectDistinct = "select distinct ", 
        from = "\nfrom ",
        tableAlias = " ", 
        fullJoin = "\nfull join ",
        innerJoin = "\ninner join ", 
        join = "\njoin ",
        leftJoin = "\nleft join ", 
        on = "\non ", 
        where = "\nwhere ",
        groupBy = "\ngroup by ", 
        having = "\nhaving ",
        orderBy = "\norder by ", 
        desc = " desc", 
        asc = " asc",
        limit = "\nlimit ", 
        offset = "\noffset ", 
        union = "\nunion\n",
        columnAlias = " ";

    // oracle specific
    private String startWith = "\nstart with ", 
        connectBy = "\nconnect by ",
        connectByPrior = "\nconnect by prior ",
        connectByNocyclePrior = "\nconnect by nocycle prior ",
        orderSiblingsBy = "\norder siblings by ", 
        sum = "sum",
        over = "over", 
        partitionBy = "partition by ";

    private String limitTemplate = "", 
        offsetTemplate = "",
        limitOffsetTemplate = "";

    private Map<Class<?>, String> class2type = new HashMap<Class<?>, String>();

    private boolean limitAndOffsetSymbols = true;

    {
        add(Ops.NOT, "not {0}");

        // math
        add(Ops.MathOps.RANDOM, "rand()");
        add(Ops.MathOps.CEIL, "ceiling({0})");
        add(Ops.MathOps.POWER, "power({0},{1})");

        // date time
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_timestamp");

        // string
        add(Ops.SUBSTR1ARG, "substr({0},{1})");
        add(Ops.SUBSTR2ARGS, "substr({0},{1},{2})");

        add(Ops.STARTSWITH, "{0} like concat({1},'%')");
        add(Ops.ENDSWITH, "{0} like concat('%',{1})");
        add(Ops.STARTSWITH_IC, "lower({0}) like concat(lower({1}),'%')");
        add(Ops.ENDSWITH_IC, "lower({0}) like concat('%',lower({1}))");

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

    public String tableAlias() {
        return tableAlias;
    }

    public SQLTemplates tableAlias(String s) {
        tableAlias = s;
        return this;
    }

    public String columnAlias() {
        return columnAlias;
    }

    public SQLTemplates columnAlias(String s) {
        columnAlias = s;
        return this;
    }

    public String asc() {
        return asc;
    }

    public SQLTemplates asc(String s) {
        asc = s;
        return this;
    }

    public String count() {
        return count;
    }

    public SQLTemplates count(String s) {
        count = s;
        return this;
    }

    public String countStar() {
        return countStar;
    }

    public SQLTemplates countStar(String s) {
        countStar = s;
        return this;
    }

    public String desc() {
        return desc;
    }

    public SQLTemplates desc(String s) {
        desc = s;
        return this;
    }

    public String from() {
        return from;
    }

    public SQLTemplates from(String s) {
        from = s;
        return this;
    }

    public String fullJoin() {
        return fullJoin;
    }

    public SQLTemplates fullJoin(String fullJoin) {
        this.fullJoin = fullJoin;
        return this;
    }

    public String groupBy() {
        return groupBy;
    }

    public SQLTemplates groupBy(String s) {
        groupBy = s;
        return this;
    }

    public String having() {
        return having;
    }

    public SQLTemplates having(String s) {
        having = s;
        return this;
    }

    public String innerJoin() {
        return innerJoin;
    }

    public SQLTemplates innerJoin(String innerJoin) {
        this.innerJoin = innerJoin;
        return this;
    }

    public String join() {
        return join;
    }

    public SQLTemplates join(String join) {
        this.join = join;
        return this;
    }

    public String leftJoin() {
        return leftJoin;
    }

    public void leftJoin(String leftJoin) {
        this.leftJoin = leftJoin;
    }

    public String limit() {
        return limit;
    }

    public SQLTemplates limit(String limit) {
        this.limit = limit;
        return this;
    }

    public String offset() {
        return offset;
    }

    public SQLTemplates offset(String offset) {
        this.offset = offset;
        return this;
    }

    public String orderBy() {
        return orderBy;
    }

    public SQLTemplates orderBy(String s) {
        orderBy = s;
        return this;
    }

    public String select() {
        return select;
    }

    public SQLTemplates select(String s) {
        select = s;
        return this;
    }

    public String selectDistinct() {
        return selectDistinct;
    }

    public SQLTemplates selectDistinct(String s) {
        selectDistinct = s;
        return this;
    }

    public boolean supportsAlias() {
        return true;
    }

    public String union() {
        return union;
    }

    public SQLTemplates union(String union) {
        this.union = union;
        return this;
    }

    public String where() {
        return where;
    }

    public SQLTemplates where(String s) {
        where = s;
        return this;
    }

    public String on() {
        return on;
    }

    public SQLTemplates on(String s) {
        on = s;
        return this;
    }

    public String dummyTable() {
        return dummyTable;
    }

    public SQLTemplates dummyTable(String dt) {
        dummyTable = dt;
        return this;
    }

    public String limitOffsetCondition(Long limit, Long offset) {
        if (offset == null) {
            return String.format(limitTemplate, limit);
        } else if (limit == null) {
            return String.format(offsetTemplate, offset);
        } else {
            return String.format(limitOffsetTemplate, limit, offset, limit
                    + offset);
        }
    }

    public boolean limitAndOffsetSymbols() {
        return limitAndOffsetSymbols;
    }

    public SQLTemplates limitAndOffsetSymbols(boolean limitAndOffsetSymbols) {
        this.limitAndOffsetSymbols = limitAndOffsetSymbols;
        return this;
    }

    public String offsetTemplate() {
        return offsetTemplate;
    }

    public SQLTemplates offsetTemplate(String offsetTemplate) {
        this.offsetTemplate = offsetTemplate;
        return this;
    }

    public String limitTemplate() {
        return limitTemplate;
    }

    public SQLTemplates limitTemplate(String limitTemplate) {
        this.limitTemplate = limitTemplate;
        return this;
    }

    public String limitOffsetTemplate() {
        return limitOffsetTemplate;
    }

    public SQLTemplates limitOffsetTemplate(String limitOffsetTemplate) {
        this.limitOffsetTemplate = limitOffsetTemplate;
        return this;
    }

    public String startWith() {
        return startWith;
    }

    public SQLTemplates startWith(String sw) {
        this.startWith = sw;
        return this;
    }

    public String connectBy() {
        return connectBy;
    }

    public SQLTemplates connectBy(String connectBy) {
        this.connectBy = connectBy;
        return this;
    }

    public String connectByPrior() {
        return connectByPrior;
    }

    public SQLTemplates connectByPrior(String connectByPrior) {
        this.connectByPrior = connectByPrior;
        return this;
    }

    public String connectByNocyclePrior() {
        return connectByNocyclePrior;
    }

    public SQLTemplates connectByNocyclePrior(String connectByNocyclePrior) {
        this.connectByNocyclePrior = connectByNocyclePrior;
        return this;
    }

    public String orderSiblingsBy() {
        return orderSiblingsBy;
    }

    public SQLTemplates orderSiblingsBy(String orderSiblingsBy) {
        this.orderSiblingsBy = orderSiblingsBy;
        return this;
    }

    public String sum() {
        return sum;
    }

    public SQLTemplates sum(String sum) {
        this.sum = sum;
        return this;
    }

    public String over() {
        return over;
    }

    public SQLTemplates over(String over) {
        this.over = over;
        return this;
    }

    public String partitionBy() {
        return partitionBy;
    }

    public SQLTemplates partitionBy(String partitionBy) {
        this.partitionBy = partitionBy;
        return this;
    }

    public SQLTemplates newLineToSingleSpace() {
        for (Field field : SQLTemplates.class.getDeclaredFields()) {
            try {
                if (field.getType().equals(String.class)) {
                    field.set(this, field.get(this).toString().replace('\n',
                            ' '));
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this;
    }

}
