/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.serialization.OperationPatterns;
import com.mysema.query.types.operation.Ops;

/**
 * SqlOps extended OperationPatterns to provided SQL specific extensions and
 * acts as database specific Dialect for Querydsl SQL
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLPatterns extends OperationPatterns {

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
        add(Ops.NOT, "not %s");

        // math
        add(Ops.Math.RANDOM, "rand()");
        add(Ops.Math.CEIL, "ceiling(%s)");
        add(Ops.Math.POWER, "power(%s,%s)");

        // date time
        add(Ops.DateTime.CURRENT_DATE, "current_date");
        add(Ops.DateTime.CURRENT_TIME, "current_timestamp");

        // string
        add(Ops.SUBSTR1ARG, "substr(%s,%s)");
        add(Ops.SUBSTR2ARGS, "substr(%s,%s,%s)");

        add(Ops.STARTSWITH, "%s like concat(%s,'%%')");
        add(Ops.ENDSWITH, "%s like concat('%%',%s)");
        add(Ops.STARTSWITH_IC, "lower(%s) like concat(lower(%s),'%%')");
        add(Ops.ENDSWITH_IC, "lower(%s) like concat('%%',lower(%s))");

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

    public SQLPatterns tableAlias(String s) {
        tableAlias = s;
        return this;
    }

    public String columnAlias() {
        return columnAlias;
    }

    public SQLPatterns columnAlias(String s) {
        columnAlias = s;
        return this;
    }

    public String asc() {
        return asc;
    }

    public SQLPatterns asc(String s) {
        asc = s;
        return this;
    }

    public String count() {
        return count;
    }

    public SQLPatterns count(String s) {
        count = s;
        return this;
    }

    public String countStar() {
        return countStar;
    }

    public SQLPatterns countStar(String s) {
        countStar = s;
        return this;
    }

    public String desc() {
        return desc;
    }

    public SQLPatterns desc(String s) {
        desc = s;
        return this;
    }

    public String from() {
        return from;
    }

    public SQLPatterns from(String s) {
        from = s;
        return this;
    }

    public String fullJoin() {
        return fullJoin;
    }

    public SQLPatterns fullJoin(String fullJoin) {
        this.fullJoin = fullJoin;
        return this;
    }

    public String groupBy() {
        return groupBy;
    }

    public SQLPatterns groupBy(String s) {
        groupBy = s;
        return this;
    }

    public String having() {
        return having;
    }

    public SQLPatterns having(String s) {
        having = s;
        return this;
    }

    public String innerJoin() {
        return innerJoin;
    }

    public SQLPatterns innerJoin(String innerJoin) {
        this.innerJoin = innerJoin;
        return this;
    }

    public String join() {
        return join;
    }

    public SQLPatterns join(String join) {
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

    public SQLPatterns limit(String limit) {
        this.limit = limit;
        return this;
    }

    public String offset() {
        return offset;
    }

    public SQLPatterns offset(String offset) {
        this.offset = offset;
        return this;
    }

    public String orderBy() {
        return orderBy;
    }

    public SQLPatterns orderBy(String s) {
        orderBy = s;
        return this;
    }

    public String select() {
        return select;
    }

    public SQLPatterns select(String s) {
        select = s;
        return this;
    }

    public String selectDistinct() {
        return selectDistinct;
    }

    public SQLPatterns selectDistinct(String s) {
        selectDistinct = s;
        return this;
    }

    public boolean supportsAlias() {
        return true;
    }

    public String union() {
        return union;
    }

    public SQLPatterns union(String union) {
        this.union = union;
        return this;
    }

    public String where() {
        return where;
    }

    public SQLPatterns where(String s) {
        where = s;
        return this;
    }

    public String on() {
        return on;
    }

    public SQLPatterns on(String s) {
        on = s;
        return this;
    }

    public String dummyTable() {
        return dummyTable;
    }

    public SQLPatterns dummyTable(String dt) {
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

    public SQLPatterns limitAndOffsetSymbols(boolean limitAndOffsetSymbols) {
        this.limitAndOffsetSymbols = limitAndOffsetSymbols;
        return this;
    }

    public String offsetTemplate() {
        return offsetTemplate;
    }

    public SQLPatterns offsetTemplate(String offsetTemplate) {
        this.offsetTemplate = offsetTemplate;
        return this;
    }

    public String limitTemplate() {
        return limitTemplate;
    }

    public SQLPatterns limitTemplate(String limitTemplate) {
        this.limitTemplate = limitTemplate;
        return this;
    }

    public String limitOffsetTemplate() {
        return limitOffsetTemplate;
    }

    public SQLPatterns limitOffsetTemplate(String limitOffsetTemplate) {
        this.limitOffsetTemplate = limitOffsetTemplate;
        return this;
    }

    public String startWith() {
        return startWith;
    }

    public SQLPatterns startWith(String sw) {
        this.startWith = sw;
        return this;
    }

    public String connectBy() {
        return connectBy;
    }

    public SQLPatterns connectBy(String connectBy) {
        this.connectBy = connectBy;
        return this;
    }

    public String connectByPrior() {
        return connectByPrior;
    }

    public SQLPatterns connectByPrior(String connectByPrior) {
        this.connectByPrior = connectByPrior;
        return this;
    }

    public String connectByNocyclePrior() {
        return connectByNocyclePrior;
    }

    public SQLPatterns connectByNocyclePrior(String connectByNocyclePrior) {
        this.connectByNocyclePrior = connectByNocyclePrior;
        return this;
    }

    public String orderSiblingsBy() {
        return orderSiblingsBy;
    }

    public SQLPatterns orderSiblingsBy(String orderSiblingsBy) {
        this.orderSiblingsBy = orderSiblingsBy;
        return this;
    }

    public String sum() {
        return sum;
    }

    public SQLPatterns sum(String sum) {
        this.sum = sum;
        return this;
    }

    public String over() {
        return over;
    }

    public SQLPatterns over(String over) {
        this.over = over;
        return this;
    }

    public String partitionBy() {
        return partitionBy;
    }

    public SQLPatterns partitionBy(String partitionBy) {
        this.partitionBy = partitionBy;
        return this;
    }

    public SQLPatterns newLineToSingleSpace() {
        for (Field field : SQLPatterns.class.getDeclaredFields()) {
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

    @Override
    public SQLPatterns toLowerCase() {
        super.toLowerCase();
        for (Field field : SQLPatterns.class.getDeclaredFields()) {
            try {
                if (field.getType().equals(String.class)) {
                    field.set(this, field.get(this).toString().toUpperCase());
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this;
    }

    @Override
    public SQLPatterns toUpperCase() {
        super.toUpperCase();
        for (Field field : SQLPatterns.class.getDeclaredFields()) {
            try {
                if (field.getType().equals(String.class)) {
                    field.set(this, field.get(this).toString().toUpperCase());
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this;
    }

}
