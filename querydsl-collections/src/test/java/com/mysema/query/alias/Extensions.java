package com.mysema.query.alias;

import com.mysema.query.types.EBoolean;
import com.mysema.query.types.EComparable;
import com.mysema.query.types.ENumber;
import com.mysema.query.types.EString;
import com.mysema.query.types.Expr;

public final class Extensions {
    
    private Extensions(){}
    
    public static <T> EBoolean having(T arg, EBoolean rv){
        return rv;
    }    

    public static <T> EBoolean eq(T val){
        return Alias.<Expr<T>>$().eq(val);
    }
    
    public static <T> EBoolean ne(T val){
        return Alias.<Expr<T>>$().ne(val);
    }    
    
    public static <T extends Number & Comparable<T>> EBoolean gt(T val){
        return Alias.<ENumber<T>>$().gt(val);
    }
    
    public static <T extends Number & Comparable<T>> EBoolean lt(T val){
        return Alias.<ENumber<T>>$().gt(val);
    }
    
    public static <T extends Number & Comparable<T>> EBoolean goe(T val){
        return Alias.<ENumber<T>>$().loe(val);
    }
    
    public static <T extends Number & Comparable<T>> EBoolean loe(T val){
        return Alias.<ENumber<T>>$().loe(val);
    }
    
    public static EBoolean startsWith(String str){
        return Alias.<EString>$().startsWith(str);
    }
    
    public static EBoolean endsWith(String str){
        return Alias.<EString>$().endsWith(str);
    }

    public static <T extends Comparable<T>> EBoolean gt(T val){
        return Alias.<EComparable<T>>$().gt(val);
    }
    
    public static <T extends Comparable<T>> EBoolean lt(T val){
        return Alias.<EComparable<T>>$().lt(val);
    }
    
    public static <T extends Comparable<T>> EBoolean goe(T val){
        return Alias.<EComparable<T>>$().gt(val);
    }
    
    public static <T extends Comparable<T>> EBoolean loe(T val){
        return Alias.<EComparable<T>>$().lt(val);
    }

}
