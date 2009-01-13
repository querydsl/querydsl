/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.lang.reflect.Field;

import com.mysema.query.serialization.OperationPatterns;

/**
 * SqlOps provides
 *
 * @author tiwe
 * @version $Id$
 */
public class SqlOps extends OperationPatterns {
    
    private String selectCountStar = "select count(*) ", 
        select = "select ",
        from = "\nfrom ",
        aliasAs = " ",
        fullJoin = "\n  full join ",
        innerJoin = "\n  inner join ",
        join = "\n  join ",
        leftJoin = "\n  left join ",
        with = "\nwith ",
        where = "\nwhere ",
        groupBy = "\ngroup by ",
        having = "\nhaving ",
        orderBy = "\norder by ",
        desc = " desc ",
        asc = " asc ";
    
    @Override
    public SqlOps toUpperCase(){
        super.toUpperCase();        
        for (Field field : SqlOps.class.getDeclaredFields()){            
            try {
                field.set(this, field.get(this).toString().toUpperCase());
            } catch (Exception e) {
                throw new RuntimeException("error", e);
            }
        }
        return this;
    }
    
    @Override
    public SqlOps toLowerCase(){
        super.toLowerCase();
        for (Field field : SqlOps.class.getDeclaredFields()){            
            try {
                field.set(this, field.get(this).toString().toUpperCase());
            } catch (Exception e) {
                throw new RuntimeException("error", e);
            }
        }
        return this;
    }
    
    public String selectCountStar() {
        return selectCountStar;
    }
    
    public SqlOps selectCountStar(String s){
        selectCountStar = s;
        return this;
    }

    public String select() {
        return select;
    }
    
    public SqlOps select(String s){
        select = s;
        return this;
    }

    public String from() {
        return from;
    }
    
    public SqlOps from(String s){
        from = s;
        return this;
    }
    
    public String aliasAs(){
        return aliasAs;
    }
    
    public SqlOps aliasAs(String s){
        aliasAs = s;
        return this;
    }

    public String with() {
        return with;
    }
    
    public SqlOps with(String s){
        with = s;
        return this;
    }

    public String where() {
        return where;
    }
    
    public SqlOps where(String s){
        where = s;
        return this;
    }

    public String groupBy() {
        return groupBy;
    }
    
    public SqlOps groupBy(String s){
        groupBy = s;
        return this;
    }

    public String having() {
        return having;
    }
    
    public SqlOps having(String s){
        having = s;
        return this;
    }

    public String orderBy() {
        return orderBy;
    }
    
    public SqlOps orderBy(String s){
        orderBy = s;
        return this;
    }

    public String desc() {
        return desc;
    }
    
    public SqlOps desc(String s){
        desc = s;
        return this;
    }

    public String asc() {
        return asc;
    }
    
    public SqlOps asc(String s){
        asc = s;
        return this;
    }

    public String fullJoin() {
        return fullJoin;
    }

    public SqlOps fullJoin(String fullJoin) {
        this.fullJoin = fullJoin;
        return this;
    }

    public String innerJoin() {
        return innerJoin;
    }

    public SqlOps innerJoin(String innerJoin) {
        this.innerJoin = innerJoin;
        return this;
    }

    public String join() {
        return join;
    }

    public SqlOps join(String join) {
        this.join = join;
        return this;
    }

    public String leftJoin() {
        return leftJoin;
    }

    public void leftJoin(String leftJoin) {
        this.leftJoin = leftJoin;
    }
    
    
    
    
    
    

}
