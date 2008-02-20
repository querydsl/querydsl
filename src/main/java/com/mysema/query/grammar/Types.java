package com.mysema.query.grammar;



/**
 * Types provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Types {
    
    public static class AsExpr<D> extends Reference<D> implements EntityPathExpr<D>{
        public Reference<D> from, to;
        AsExpr(Reference<D> from, Reference<D> to) {
            super(to._path);
        }        
    }
    
    /**
     * Binary boolean expression (expression with two arguments and boolean type)
     */
    public static class BooleanBinaryExpr implements Expr<Boolean>{
        public Expr<Boolean> left;
        public Expr<Boolean> right; 
        public Op<Boolean> type;        
    }
    
    public static class BooleanProperty extends Reference<Boolean>{
        public BooleanProperty(String path) {super(path);}
    }
    
    /**
     * Unary boolean expression (expression with one argument and boolean type)
     */
    public static class BooleanUnaryExpr implements Expr<Boolean>{
        public Expr<Boolean> left;
        public Op<Boolean> type;                
    }
    
    /**
     * Boolean operators (operators used with boolean operands)
     */
    public interface BoOp<RT> extends Op<RT>{ 
        BoOp<Boolean> AND = new BoOpImpl<Boolean>(); 
        BoOp<Boolean> NOT = new BoOpImpl<Boolean>();
        BoOp<Boolean> OR = new BoOpImpl<Boolean>();
        BoOp<Boolean> XNOR = new BoOpImpl<Boolean>();
        BoOp<Boolean> XOR = new BoOpImpl<Boolean>();
    }
    
    public static class CharProperty extends Reference<Character>{
        public CharProperty(String path) {super(path);}        
    }
    
    public static class ConstantExpr<A> implements Expr<A>{
        public A constant;
    }
    
    public static class DomainType<D> extends Reference<D> implements EntityPathExpr<D>{
        protected DomainType(DomainType<?> type, String path) {
            super(type._path+"."+path);
        } 
        protected DomainType(String path) {super(path);}
        public EntityPathExpr<D> as(DomainType<D> to){
            return new AsExpr<D>(this, to);
        }
    }    
        
    public static interface EntityPathExpr<T> extends Expr<T>{}
    
    public interface Expr<A> { }
    
    public static class NumberProperty extends Reference<Number>{
        public NumberProperty(String path) {super(path);} 
    }
       
    /**
     * Numeric Operators (operators used with numeric operands)
     */
    public interface NumOp<RT> extends Op<RT>{
        NumOp<Number> ADD = new NumOpImpl<Number>();   
        NumOp<Number> DIV = new NumOpImpl<Number>();
        NumOp<Boolean> GOE = new NumOpImpl<Boolean>();
        NumOp<Boolean> GT = new NumOpImpl<Boolean>();
        NumOp<Boolean> LOE = new NumOpImpl<Boolean>();
        NumOp<Boolean> LT = new NumOpImpl<Boolean>();
        NumOp<Number> MOD = new NumOpImpl<Number>();
        NumOp<Number> MULT = new NumOpImpl<Number>();
        NumOp<Number> SUB = new NumOpImpl<Number>();
    }
    
    /**
     * Operators (the return type is encoded in the generic parameter)
     */
    public interface Op<RT> {
        Op<Boolean> EQ = new OpImpl<Boolean>();
        Op<Boolean> NE = new OpImpl<Boolean>();
    }
    
    public enum Order{ ASC,DESC }       
    
    public static class OrderSpecifier<A>{
        public Order order; 
        public Expr<A> target;       
    }
    
    public static class Reference<T> implements Expr<T>{        
        public final String _path;
        public Reference(String path) {
            this._path = path;
        }
        protected BooleanProperty bool(String path){
            return new BooleanProperty(this._path+"."+_path);
        }
        protected CharProperty ch(String path) {
            return new CharProperty(this._path+"."+path);
        }
        protected NumberProperty num(String path) {
            return new NumberProperty(this._path+"."+path);
        }
        protected StringProperty str(String path) {
            return new StringProperty(this._path+"."+path);
        }
    }
    
    public static class StringProperty extends Reference<String>{
        public StringProperty(String path) {super(path);}        
    }
    
    /**
     * String Operators (operators used with String operands)
     */
    public interface StrOp<RT> extends Op<RT>{       
        StrOp<String> CONCAT = new StrOpImpl<String>();
        StrOp<Boolean> LIKE = new StrOpImpl<Boolean>();
        StrOp<String> LOWER = new StrOpImpl<String>();
        StrOp<String> SUBSTRING = new StrOpImpl<String>();
        StrOp<String> UPPER = new StrOpImpl<String>();
    }

}
