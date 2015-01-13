package com.querydsl.sql;

public class Survey {
    
    @Column("NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}