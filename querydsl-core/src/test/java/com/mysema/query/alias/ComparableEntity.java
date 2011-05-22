package com.mysema.query.alias;

public class ComparableEntity implements Comparable<ComparableEntity>{

    @Override
    public int compareTo(ComparableEntity o) {
        return 0;
    }
    
    public String getProperty(){
        return "";
    }

}
