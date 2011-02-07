/**
 * 
 */
package com.mysema.query.types;

public class ProjectionExample{

    public Long id;
    
    public String text;
    
    public ProjectionExample(){

    }

    public ProjectionExample(Long id){
        this.id = id;
    }

    public ProjectionExample(long id, String text){
        this.id = id;
        this.text = text;
    }

    public ProjectionExample(CharSequence text){
        this.text = text.toString();
    }
}