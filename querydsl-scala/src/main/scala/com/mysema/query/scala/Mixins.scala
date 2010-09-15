package com.mysema.query.scala;

import com.mysema.query.types._;
import com.mysema.util.ReflectionUtils;

class PathMixin[T](t: Class[T], metadata : PathMetadata[_]) extends Path[T] {
    
    def accept[R,C](v: Visitor[R,C], c: C) = v.visit(this, c);
    
    def getAnnotatedElement() = null;
    
    def getMetadata() = metadata;
    
    def getRoot(): Path[_] = { if (metadata.getRoot != null) metadata.getRoot else this };
    
    def getType() = t;
}

class OperationMixin[T](t: Class[T], operator: Operator[T], args: Array[Expression[_]]) extends Operation[T] {
    
    def accept[R,C](v: Visitor[R,C], c: C) = v.visit(this, c);
    
    def getArg(i: Int) = args(i);
    
    def getArgs() = java.util.Arrays.asList(args:_*);
    
    def getOperator() = operator; 
    
    def getType() = t;
    
}

// TODO : SubQueryMixin

// TODO : ConstantMixin