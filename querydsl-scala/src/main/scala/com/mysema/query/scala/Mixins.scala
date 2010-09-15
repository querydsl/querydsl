package com.mysema.query.scala;

import com.mysema.query.types._;
import com.mysema.util.ReflectionUtils;

abstract class PathMixin[T](t: Class[T], metadata : PathMetadata[_]) extends Path[T] {
    
    //def [R,C]accept(v: Visitor[R,C], c: C) = v.visit(this, c);
    
    def getAnnotatedElement() = null;
    
    def getMetadata() = metadata;
    
    def getRoot() = if (metadata.getRoot != null) metadata.getRoot else this;
    
    def getType() = t;
}

abstract class OperationMixin[T](t: Class[T], operator: Operator[T], args: Array[Expression[_]]) extends Operation[T] {
    
    //def [R,C]accept(v: Visitor[R,C], c: C) = v.visit(this, c);
    
    def getArg(i: Int) = args(i);
    
    //def getArgs() = java.util.Arrays.asList(args);
    
    def getOperator() = operator; 
    
    def getType() = t;
    
}