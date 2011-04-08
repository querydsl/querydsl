package com.mysema.query.codegen;

import com.mysema.query.annotations.QuerySupertype;

@QuerySupertype
public class ExampleSupertype {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
