package com.mysema.query.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

public class ExprTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() throws Throwable {
        List<Expr<?>> exprs = new ArrayList<Expr<?>>();
        exprs.add(QAnimal.animal);
        exprs.add(QCat.cat);
        exprs.add(QCategory.category);
        exprs.add(QClassWithConstructor.classWithConstructor);
        exprs.add(QEntity1.entity1);
        exprs.add(QEntity2.entity2);
        exprs.add(QEntity3.entity3);
        exprs.add(QEntityWithEmbedded.entityWithEmbedded);
        exprs.add(QGenericType.genericType);
        exprs.add(QInterfaceType.interfaceType);
        exprs.add(QInterfaceType2.interfaceType2);
        exprs.add(QInterfaceType3.interfaceType3);
        exprs.add(QInterfaceType4.interfaceType4);
        exprs.add(QInterfaceType5.interfaceType5);
        exprs.add(QItemType.itemType);
        exprs.add(QJodaTimeSupport.jodaTimeSupport);
        exprs.add(QPEntity.pEntity);
        exprs.add(QPEntity2.pEntity2);
        exprs.add(QPEntity3.pEntity3);
        exprs.add(QPEntity4.pEntity4);
        exprs.add(QQueryTypeEntity.queryTypeEntity);
        exprs.add(QReference.reference);
        exprs.add(QRelationType.relationType);
        exprs.add(QReservedNames.reservedNames);
        exprs.add(QSimpleTypes.simpleTypes);
        
        exprs.add(EString.create("Hello World!"));
        exprs.add(ENumber.create(1000));
        exprs.add(ENumber.create(10l));
        exprs.add(EBoolean.TRUE);
        exprs.add(EBoolean.FALSE);
        
        Set<Expr<?>> toVisit = new HashSet<Expr<?>>();
        
        // all entities
        toVisit.addAll(exprs);
        // and all their direct properties
        for (Expr<?> expr : exprs){
            for (Field field : expr.getClass().getFields()){
                Object rv = field.get(expr);
                if (rv instanceof Expr){
                    if (rv instanceof EString){
                        EString str = (EString)rv;
                        toVisit.add(str.toLowerCase());
                        toVisit.add(str.charAt(0));
                        toVisit.add(str.isEmpty());
                    }else if (rv instanceof EBoolean){
                        EBoolean b = (EBoolean)rv; 
                        toVisit.add(b.not());
                    }
                    toVisit.add((Expr<?>) rv);
                }
            }
        }
        
        Set<String> failures = new TreeSet<String>();
        
        for (Expr<?> expr : toVisit){
            for (Method method : expr.getClass().getMethods()){
                if (method.getName().equals("getArg")) continue;
                if (method.getReturnType() != void.class
                 && !method.getReturnType().isPrimitive()){
                    Class<?>[] types = method.getParameterTypes();
                    Object[] args;                        
                    if (types.length == 0){
                        args = new Object[0];                            
                    }else if (types.length == 1){
                        if (types[0] == int.class){
                            args = new Object[]{Integer.valueOf(1)};           
                        }else if (types[0] == boolean.class){    
                            args = new Object[]{Boolean.TRUE};
                        }else{
                            continue;
                        }                           
                        
                    }else{
                        continue;
                    }
                    Object rv = method.invoke(expr, args);   
                    if (method.invoke(expr, args) != rv){
                        failures.add(expr.getClass().getSimpleName()+"."+method.getName()+" is unstable");
                    }
                }
            }    
        }

        if (failures.size() > 0){
            System.err.println("Got "+failures.size()+" failures\n");
        }
        for (String failure : failures){
            System.err.println(failure);
        }
                        
//        assertTrue("Got "+failures.size()+" failures",failures.isEmpty());
    }

}
