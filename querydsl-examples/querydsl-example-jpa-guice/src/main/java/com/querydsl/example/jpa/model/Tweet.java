package com.querydsl.example.jpa.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tweet")
public class Tweet extends BaseEntity {
    private String content;

    @ManyToOne(optional = false)
    private User poster;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> mentions = new ArrayList<User>();

    @ManyToOne
    private Location location;

    public Tweet() {
    }

    public Tweet(User poster, String content, List<User> mentions,
            Location location) {
        this.poster = poster;
        this.content = content;
        this.mentions = mentions;
        this.location = location;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public void setMentions(List<User> mentions) {
        this.mentions = mentions;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getPoster() {
        return poster;
    }

    public List<User> getMentions() {
        return mentions;
    }

    public String getContent() {
        return content;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
