/**
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;

@QueryEntity
public class Comment {
    
    private int id;
    
    private String text;
    
    private User user;
    
    private Post post;

    public Comment() {}
    
    @QueryProjection
    public Comment(int id, String text) {
        this.id = id;
        this.text = text;
    }
    
    @QueryProjection
    public Comment(int id, String text, User user, Post post) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.post = post;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    
    
}