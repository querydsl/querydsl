package com.mysema.query.serialization;

import java.util.ArrayList;
import java.util.List;

import com.mysema.commons.lang.Assert;

/**
 * PatternElement provides
 *
 * @author tiwe
 * @version $Id$
 */
public class OperationPattern {

//    private static final Pattern patter
    
    private final String pattern;
    
    private final List<Element> elements = new ArrayList<Element>();
    
    public OperationPattern(String pattern){
        this.pattern = Assert.notNull(pattern);
    }
    
    public int hashCode(){
        return pattern.hashCode();
    }
    
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof OperationPattern){
            return ((OperationPattern)o).pattern.equals(pattern);
        }else{
            return false;
        }
    }
    
    public class Element{
        
        private final int index;
        
        private final String text;
        
        public Element(int index, String text){
            this.index = index;
            this.text = text;            
        }
        
        public int getIndex() {
            return index;
        }

        public String getText() {
            return text;
        }
        
    }
}
