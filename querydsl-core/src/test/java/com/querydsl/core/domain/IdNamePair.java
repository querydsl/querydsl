package com.querydsl.core.domain;

public class IdNamePair<Type> {

    private String id;
    
    private Type name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getName() {
        return name;
    }

    public void setName(Type name) {
        this.name = name;
    }
    
    
    
}
