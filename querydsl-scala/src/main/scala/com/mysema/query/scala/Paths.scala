package com.mysema.query.scala;

import com.mysema.query.types._;
import com.mysema.query.types.PathMetadataFactory._;

/**
 * @author tiwe
 *
 */
object Paths {

    def simple[T](t: Class[_ <: T], md: PathMetadata[_]) = new SimplePath[T](t, md);
    
    def comparable[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_]) = new ComparablePath[T](t, md);
    
    def date[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_]) = new DatePath[T](t, md);
    
    def dateTime[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_]) = new DateTimePath[T](t, md);
    
    def time[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_]) = new TimePath[T](t, md);
    
    def number[T <: Number with Comparable[T]](t: Class[_ <: T], md: PathMetadata[_]) = new NumberPath[T](t, md);
    
    def boolean(md: PathMetadata[_]) = new BooleanPath(md);
    
    def string(md: PathMetadata[_]) = new StringPath(md); 
    
    def enum[T <: Enum[T]](t: Class[T], md: PathMetadata[_]) = new EnumPath[T](t, md);
}

class SimplePath[T](t: Class[_ <: T], md: PathMetadata[_] ) 
    extends PathImpl[T](t, md) with SimpleExpression[T]{
    
    def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));
    
}

class ComparablePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_] ) 
    extends PathImpl[T](t, md) with ComparableExpression[T]{
    
    def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));
    
}

class NumberPath[T <: Number with Comparable[T]](t: Class[_ <: T], md: PathMetadata[_] ) 
    extends PathImpl[T](t, md) with NumberExpression[T]{

    def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));
}

class BooleanPath(md: PathMetadata[_] ) 
    extends PathImpl[java.lang.Boolean](classOf[java.lang.Boolean], md) with BooleanExpression{
    
    def this(variable: String) = this(forVariable(variable));
    
}

class StringPath(md: PathMetadata[_] ) 
    extends PathImpl[String](classOf[String], md) with StringExpression{
    
    def this(variable: String) = this(forVariable(variable));
    
}

class DatePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_] ) 
    extends PathImpl[T](t, md) with DateExpression[T]{
    
    def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));
    
}

class DateTimePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_] ) 
    extends PathImpl[T](t, md) with DateTimeExpression[T]{
    
    def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));
    
}

class TimePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_] ) 
    extends PathImpl[T](t, md) with TimeExpression[T]{
    
    def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));
    
}

class EnumPath[T <: Enum[T]](t: Class[_ <: T], md: PathMetadata[_] ) 
    extends PathImpl[T](t, md) with EnumExpression[T]{
    
    def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));
    
}

// TODO : ListPath

// TODO : SetPath

// TODO : CollectionPath