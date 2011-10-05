/**
 * 
 */
package com.mysema.query.group;

import java.util.Set;

public class Post {
    
    private Integer id;
    
    private String name;
    
    private Set<Comment> comments;

    public Post() {
        
    }
    
    public Post(Integer id, String name, Set<Comment> comments) {
        this.id = id;
        this.name = name;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
    
    
}