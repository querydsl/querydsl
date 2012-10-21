package com.mysema.query.collections;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;

@QueryEntity
public class User2 extends User {
    private String email;

    @QueryProjection
    public User2(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
