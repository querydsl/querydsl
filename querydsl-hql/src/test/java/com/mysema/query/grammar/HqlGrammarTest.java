package com.mysema.query.grammar;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


/**
 * HqlGrammarTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlGrammarTest {
    
    @Test
    public void noDuplicates(){
        Set<String> names = new HashSet<String>();
        for (Method method : Grammar.class.getDeclaredMethods()){
            names.add(method.getName());
        }
        
        for (Method method : HqlGrammar.class.getDeclaredMethods()){
            if (names.contains(method.getName())){
                System.out.println(method.getName() + " already available.");
            }
        }
    }

}
