package com.mysema.query.grammar;



/**
 * Query provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Types {
    
    // order
    
    public enum Order{ ASC,DESC }
    
    public static class OrderSpecifier<A>{
        public Order order; 
        public Expr<A> target;       
    }
    
    // expressions
    
    public interface Expr<A> { }
    
    // constant
    
    public static class ConstantExpr<A> implements Expr<A>{
        public A constant;
    }
    
    public interface Op {}
    
    // operators 
    
    public enum BoOp implements Op{ 
     // boolean
        AND, 
        NOT, 
        OR, 
        XOR, 
        XNOR, 
        
     // comparison
        EQ, 
        NE, 
        GOE, 
        GT, 
        LOE, 
        LT,
        
     // string comparison
        LIKE,
    }
    
    public enum StrOp implements Op{        
     // string manipulation
        LOWER,
        UPPER, 
    }
    
    public enum NumOp implements Op{
     // arithmetic
        PLUS, 
        MINUS, 
        MULT, 
        DIV, 
        MOD  
    }    
        
    public static class BooleanUnaryExpr implements Expr<Boolean>{
        public BoOp type;
        public Expr<Boolean> left;                
    }
    
    public static class BooleanBinaryExpr implements Expr<Boolean>{
        public BoOp type;
        public Expr<Boolean> left; public Expr<Boolean> right;        
    }
    
    // references
    
    public static interface EntityPathExpr<T> extends Expr<T>{}
       
    public static class Reference<T> implements Expr<T>{        
        public Reference(String path) {
            this._path = path;
        }
        protected CharProperty ch(String path) {
            return new CharProperty(this._path+"."+path);
        }
        protected StringProperty str(String path) {
            return new StringProperty(this._path+"."+path);
        }
        protected NumberProperty num(String path) {
            return new NumberProperty(this._path+"."+path);
        }
        protected BooleanProperty bool(String path){
            return new BooleanProperty(this._path+"."+_path);
        }
        public final String _path;
    }
    
    public static class BooleanProperty extends Reference<Boolean>{
        public BooleanProperty(String path) {super(path);}
    }
    public static class CharProperty extends Reference<Character>{
        public CharProperty(String path) {super(path);}        
    }       
    public static class NumberProperty extends Reference<Number>{
        public NumberProperty(String path) {super(path);} 
    }
    public static class StringProperty extends Reference<String>{
        public StringProperty(String path) {super(path);}        
    }
    public static class DomainType<D> extends Reference<D> implements EntityPathExpr<D>{
        protected DomainType(String path) {super(path);} 
        protected DomainType(DomainType<?> type, String path) {
            super(type._path+"."+path);
        }
        public EntityPathExpr<D> as(DomainType<D> to){
            return new AsExpr<D>(this, to);
        }
    }
    
    public static class AsExpr<D> extends Reference<D> implements EntityPathExpr<D>{
        AsExpr(Reference<D> from, Reference<D> to) {
            super(to._path);
        }
        public Reference<D> from, to;        
    }

}
