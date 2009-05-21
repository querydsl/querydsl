/**
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.Expr;

public class PComponentCollection<D> extends ESimple<java.util.Collection<D>> implements PCollection<D>{
    private EBoolean isnull, isnotnull;
    private final PathMetadata<?> metadata;
    private EComparable<Integer> size;
    protected final Class<D> type;
    private final Path<?> root;
    public PComponentCollection(Class<D> type, PathMetadata<?> metadata) {
        super(null);            
        this.type = type;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }
    public PComponentCollection(Class<D> type, String var){
        this(type, PathMetadata.forVariable(var));   
    }        
    public Class<D> getElementType() {return type;}
    public PathMetadata<?> getMetadata() {return metadata;}
    public EBoolean isnotnull() {
        if (isnotnull == null) isnotnull = Grammar.isnotnull(this);
        return isnotnull; 
    }
    public EBoolean isnull() {
        if (isnull == null) isnull = Grammar.isnull(this);
        return isnull; 
    }     
    public EComparable<Integer> size() {
        if (size == null) size = new PComparable<Integer>(Integer.class, PathMetadata.forSize(this));
        return size;
    }
    public EBoolean contains(D child) {
        return Grammar.in(child, this);
    }
    public EBoolean contains(Expr<D> child) {
        return Grammar.in(child, this);
    }
    public Path<?> getRoot(){
        return root;
    }
    public int hashCode(){
        return metadata.hashCode();
    }        
    public boolean equals(Object o){
        return o instanceof Path ? ((Path<?>)o).getMetadata().equals(metadata) : false;
    }
}