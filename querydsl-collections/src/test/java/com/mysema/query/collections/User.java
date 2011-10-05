/**
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;


@QueryEntity
public class User {
    
    private String name;
    
    private Post latestPost;
    
    public User() {}
    
    @QueryProjection
    public User(String name) {
        this.name = name;
    }
    
    @QueryProjection
    public User(String name, Post latestPost) {
        this.name = name;
        this.latestPost = latestPost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Post getLatestPost() {
        return latestPost;
    }

    public void setLatestPost(Post latestPost) {
        this.latestPost = latestPost;
    }

    
            
}