package com.mysema.query.group;

public class Comment {
    
    private  Integer id;
    
    private String text;

    public Comment() {}
    
    public Comment(Integer id, String text) {
        this.id = id;
        this.text = text;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode() + text.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Comment) {
            Comment other = (Comment) o;
            return this.id.equals(other.id) && this.text.equals(other.text);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return id + ": " + text;
    }
}