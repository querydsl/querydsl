/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.codegen.EntityType;

/**
 * @author tiwe
 *
 */
public abstract class AbstractNamingStrategy implements NamingStrategy {
    
    protected String foreignKeysClassName = "ForeignKeys";
    
    protected String foreignKeysVariable = "fk";
    
    protected String primaryKeysClassName = "PrimaryKeys";
    
    protected String primaryKeysVariable = "pk";
    
    protected String reservedSuffix = "_col";
    
    @Override
    public String getForeignKeysClassName() {
        return foreignKeysClassName;
    }

    @Override
    public String getForeignKeysVariable(EntityType entityType) {
        return foreignKeysVariable;
    }

    @Override
    public String getPrimaryKeysClassName() {
        return primaryKeysClassName;
    }

    @Override
    public String getPrimaryKeysVariable(EntityType entityType) {
        return primaryKeysVariable;
    }
    
    public void setForeignKeysClassName(String foreignKeysClassName) {
        this.foreignKeysClassName = foreignKeysClassName;
    }

    public void setForeignKeysVariable(String foreignKeysVariable) {
        this.foreignKeysVariable = foreignKeysVariable;
    }

    public void setPrimaryKeysClassName(String primaryKeysClassName) {
        this.primaryKeysClassName = primaryKeysClassName;
    }

    public void setPrimaryKeysVariable(String primaryKeysVariable) {
        this.primaryKeysVariable = primaryKeysVariable;
    }

    public void setReservedSuffix(String reservedSuffix) {
        this.reservedSuffix = reservedSuffix;
    }

}
