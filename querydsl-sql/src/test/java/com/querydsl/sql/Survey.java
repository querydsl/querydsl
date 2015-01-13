package com.querydsl.sql;

import com.querydsl.sql.Column;

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