/**
 * 
 */
package com.mysema.query.types;

public class Projection{

    public Long id;
    
    public String text;
    
    public Projection(){

    }

    public Projection(Long id){
        this.id = id;
    }

    public Projection(long id, String text){
        this.id = id;
        this.text = text;
    }

    public Projection(CharSequence text){
        this.text = text.toString();
    }
}