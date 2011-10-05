package com.mysema.query.group;


/**
 * TODO: How to project an inner group, i.e. [User] 1->1 [Post] 1->N [Comment]
 */
public class User {
    
    private String name;
    
    private Post latestPost;

    public User() {
        
    }
    
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