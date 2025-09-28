/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.apt.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.Test;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;

public class ExpressionTest {

    @Test
    public void test() throws Throwable {
        List<Expression<?>> exprs = new ArrayList<Expression<?>>();
        exprs.add(QAnimalTest_Animal.Constants.animal);
        exprs.add(QAnimalTest_Cat.Constants.cat);
        exprs.add(QConstructorTest_Category.Constants.category);
        exprs.add(QConstructorTest_ClassWithConstructor.Constants.classWithConstructor);
        exprs.add(QEntityTest_Entity1.Constants.entity1);
        exprs.add(QEntityTest_Entity2.Constants.entity2);
        exprs.add(QEntityTest_Entity3.Constants.entity3);
        exprs.add(QEmbeddableTest_EntityWithEmbedded.Constants.entityWithEmbedded);
        exprs.add(QGenericTest_GenericType.Constants.genericType);
        exprs.add(QInterfaceTypeTest_InterfaceType.Constants.interfaceType);
        exprs.add(QInterfaceTypeTest_InterfaceType2.Constants.interfaceType2);
        exprs.add(QInterfaceTypeTest_InterfaceType3.Constants.interfaceType3);
        exprs.add(QInterfaceTypeTest_InterfaceType4.Constants.interfaceType4);
        exprs.add(QInterfaceTypeTest_InterfaceType5.Constants.interfaceType5);
        exprs.add(QGenericTest_ItemType.Constants.itemType);
        exprs.add(QJodaTimeSupportTest_JodaTimeSupport.Constants.jodaTimeSupport);
        exprs.add(QQueryInitTest_PEntity.Constants.pEntity);
        exprs.add(QQueryInitTest_PEntity2.Constants.pEntity2);
        exprs.add(QQueryInitTest_PEntity3.Constants.pEntity3);
        exprs.add(QQueryInitTest_PEntity4.Constants.pEntity4);
        exprs.add(QQueryTypeTest_QueryTypeEntity.Constants.queryTypeEntity);
        exprs.add(QRelationTest_Reference.Constants.reference);
        exprs.add(QRelationTest_RelationType.Constants.relationType);
        exprs.add(QReservedNamesTest_ReservedNames.Constants.reservedNames);
        exprs.add(QSimpleTypesTest_SimpleTypes.Constants.simpleTypes);

        exprs.add(ConstantImpl.create("Hello World!"));
        exprs.add(ConstantImpl.create(1000));
        exprs.add(ConstantImpl.create(10L));
        exprs.add(ConstantImpl.create(true));
        exprs.add(ConstantImpl.create(false));

        // all entities
        Set<Expression<?>> toVisit = new HashSet<Expression<?>>(exprs);
        // and all their direct properties
        for (Expression<?> expr : exprs) {
            for (Field field : expr.getClass().getFields()) {
                Object rv = field.get(expr);
                if (rv instanceof Expression) {
                    if (rv instanceof StringExpression) {
                        StringExpression str = (StringExpression) rv;
                        toVisit.add(str.toLowerCase());
                        toVisit.add(str.charAt(0));
                        toVisit.add(str.isEmpty());
                    } else if (rv instanceof BooleanExpression) {
                        BooleanExpression b = (BooleanExpression) rv;
                        toVisit.add(b.not());
                    }
                    toVisit.add((Expression<?>) rv);
                }
            }
        }

        Set<String> failures = new TreeSet<String>();

        for (Expression<?> expr : toVisit) {
            for (Method method : expr.getClass().getMethods()) {
                if (method.getName().equals("getParameter")) {
                    continue;
                }
                if (method.getName().equals("getArg")) {
                    continue;
                }
                if (method.getReturnType() != void.class
                 && !method.getReturnType().isPrimitive()) {
                    Class<?>[] types = method.getParameterTypes();
                    Object[] args;
                    if (types.length == 0) {
                        args = new Object[0];
                    } else if (types.length == 1) {
                        if (types[0] == int.class) {
                            args = new Object[]{1};
                        } else if (types[0] == boolean.class) {
                            args = new Object[]{Boolean.TRUE};
                        } else {
                            continue;
                        }

                    } else {
                        continue;
                    }
                    Object rv = method.invoke(expr, args);
                    if (method.invoke(expr, args) != rv) {
                        failures.add(expr.getClass().getSimpleName() + "." + method.getName() + " is unstable");
                    }
                }
            }
        }

        if (failures.size() > 0) {
            System.err.println("Got " + failures.size() + " failures\n");
        }
        for (String failure : failures) {
            System.err.println(failure);
        }

//        assertTrue("Got "+failures.size()+" failures",failures.isEmpty());
    }

}
