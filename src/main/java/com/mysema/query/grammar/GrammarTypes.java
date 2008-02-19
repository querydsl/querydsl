package com.mysema.query.grammar;



/**
 * Query provides
 *
 * @author tiwe
 * @version $Id$
 */
public class GrammarTypes {
    
    // order
    
    public enum Order{ ASC,DESC }
    
    public static class OrderSpecifier{
        public  Order order;
        public  Expr target;
    }
    
    // expressions
    
    public interface Expr { }
    
    // constant
    
    public static class ConstantExpr implements Expr{
        public Object constant;
    }
    
    // boolean 

    public interface BooleanExpr extends Expr{ }
    
    public enum BooleanExprType { AND, EQ, GOE, GT,LOE, LT, NE, NOT, OR, XOR }

    public static class BooleanUnaryExpr implements BooleanExpr{
        public BooleanExprType type;
        public Expr left;                
    }
    
    public static class BooleanBinaryExpr implements BooleanExpr{
        public BooleanExprType type;
        public Expr left; public Expr right;        
    }
    
    // references
       
    public static class Reference<T> implements Expr{        
        public Reference(String path) {
            this._path = path;
        }
        protected CharProperty charProp(String path) {
            return new CharProperty(this._path+"."+path);
        }
        protected StringProperty stringProp(String path) {
            return new StringProperty(this._path+"."+path);
        }
        protected NumberProperty numberProp(String path) {
            return new NumberProperty(this._path+"."+path);
        }
//        protected CollectionProperty colProp(String path) {
//            return new CollectionProperty(this._path+"."+path);
//        }
        public final String _path;
    }
    public static class CharProperty extends Reference<Character>{
        public CharProperty(String path) {super(path);}        
    }    
    public static class StringProperty extends Reference<String>{
        public StringProperty(String path) {super(path);}        
    }
    public static class NumberProperty extends Reference<Number>{
        public NumberProperty(String path) {super(path);} 
    }
//    public static class CollectionProperty extends Reference<Collection<?>>{
//        public CollectionProperty(String path) {super(path);} 
//    }
    public static class DomainType<D> extends Reference<D>{
        public DomainType(String path) {super(path);} 
        public DomainType<D> as(DomainType<D> d){
            // TODO
            return d;
        }
    }

}
