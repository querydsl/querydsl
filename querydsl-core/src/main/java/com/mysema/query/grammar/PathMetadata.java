/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import static com.mysema.query.grammar.Grammar.*;
import com.mysema.query.grammar.Types.Expr;
import com.mysema.query.grammar.Types.Path;

/**
 * 
 * PathMetadata provides
 *
 * @author tiwe
 * @version $Id$
 *
 */
public final class PathMetadata<T>{
    private final Expr<T> expression;
    private final Path<?> parent;
    private final Type type;    
    
    private PathMetadata(Path<?> parent, Expr<T> expression, Type type){
        this.parent = parent;
        this.expression = expression;
        this.type = type;
    }
    
    public static PathMetadata<Integer> forListAccess(Path<?> parent, Expr<Integer> index){
        return new PathMetadata<Integer>(parent,index,Type.LISTACCESS);
    }
    
    public static PathMetadata<Integer> forListAccess(Path<?> parent, int index){
        return new PathMetadata<Integer>(parent,_const(index),Type.LISTACCESSC);
    }
    
    public static <KT> PathMetadata<KT> forMapAccess(Path<?> parent, Expr<KT> key){
        return new PathMetadata<KT>(parent,key,Type.MAPACCESS);
    }
    
    public static <KT> PathMetadata<KT> forMapAccess(Path<?> parent, KT key){
        return new PathMetadata<KT>(parent,_const(key),Type.MAPACCESSC);
    }
    
    public static PathMetadata<String> forProperty(Path<?> parent, String property){
        return new PathMetadata<String>(parent,_const(property),Type.PROPERTY);
    }
    
    public static PathMetadata<String> forVariable(String variable){
        return new PathMetadata<String>(null,_const(variable),Type.VARIABLE);
    }
    
    public Expr<T> getExpression() {return expression;}
    
    public Path<?> getParent(){ return parent; }
    
    public Type getType() {return type;}  
    
    public enum Type{
        LISTACCESS,LISTACCESSC,MAPACCESS,MAPACCESSC,PROPERTY,VARIABLE
    }    
    
}
