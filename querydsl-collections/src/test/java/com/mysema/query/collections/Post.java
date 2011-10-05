/**
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;

@QueryEntity
public class Post {
    
    private int id;
    
    private String name;
    
    private User user;
    
    public Post() {}
    
    @QueryProjection
    public Post(int id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}