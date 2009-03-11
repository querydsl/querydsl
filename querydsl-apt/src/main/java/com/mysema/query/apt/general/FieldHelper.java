package com.mysema.query.apt.general;

/**
 * FieldHelper provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FieldHelper {

    public static String javaSafe(String name){
        // TODO : improve this
        if (name.equals("private")){
            return "prvate";
        }else if (name.equals("public")){
            return "pblic";
        }else{
            return name;
        }
    }
    
    public static String realName(String name){
        // TODO : improve this
        if (name.equals("prvate")){
            return "private";
        }else if (name.equals("pblic")){
            return "public";
        }else{
            return name;
        }
    }
    
}
