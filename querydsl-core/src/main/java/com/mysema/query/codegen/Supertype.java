/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public class Supertype {

    private EntityType entityType;

    private final Type type;

    public Supertype(Type type) {
        this.type = type;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Type getType() {
        return type;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public int hashCode(){
        return type.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Supertype){
            return ((Supertype)o).type.equals(type);
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return type.toString();
    }

}
