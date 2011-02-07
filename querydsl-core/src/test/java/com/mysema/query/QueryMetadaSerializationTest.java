package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

import com.mysema.query.QueryFlag.Position;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.util.ReflectionUtils;

public class QueryMetadaSerializationTest {

    private QueryMetadata metadata = new DefaultQueryMetadata();

    @Test
    public void Serialization() throws IOException, ClassNotFoundException{
        StringPath expr = new StringPath("str");
        metadata.addFlag(new QueryFlag(Position.AFTER_FILTERS, ""));
        metadata.addGroupBy(expr);
        metadata.addHaving(expr.isEmpty());
        metadata.addJoin(JoinType.DEFAULT, expr);
        metadata.getJoins().get(0).addFlag(new JoinFlag(""));
        metadata.addJoinCondition(expr.isEmpty());
        metadata.addOrderBy(expr.asc());
        metadata.addProjection(expr);
        metadata.addWhere(expr.isEmpty());
        
        // serialize metadata
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(metadata);
        out.close();
        
        // deserialize metadata
        ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bain);
        QueryMetadata  metadata2 = (QueryMetadata) in.readObject();
        in.close();
        
        assertEquals(metadata.getFlags(), metadata2.getFlags());
        assertEquals(metadata.getGroupBy().get(0), metadata2.getGroupBy().get(0));
        assertEquals(metadata.getGroupBy(), metadata2.getGroupBy());
        assertEquals(metadata.getHaving(), metadata2.getHaving());
        assertEquals(metadata.getJoins(), metadata2.getJoins());
        assertEquals(metadata.getModifiers(), metadata2.getModifiers());
        assertEquals(metadata.getOrderBy(), metadata2.getOrderBy());
        assertEquals(metadata.getParams(), metadata2.getParams());
        assertEquals(metadata.getProjection(), metadata2.getProjection());
        assertEquals(metadata.getWhere(), metadata2.getWhere());
    }
    
    @Test
    public void FullySerizable(){
        Set<Class<?>> checked = new HashSet<Class<?>>();
        checked.addAll(Arrays.<Class<?>>asList(List.class, Set.class, Map.class, Object.class, String.class, Class.class));
        Stack<Class<?>> classes = new Stack<Class<?>>();
        classes.addAll(Arrays.<Class<?>>asList(NumberPath.class, NumberOperation.class, NumberTemplate.class, BeanPath.class, DefaultQueryMetadata.class));
        while (!classes.isEmpty()){            
            Class<?> clazz = classes.pop();
            checked.add(clazz);
            if (!Serializable.class.isAssignableFrom(clazz) && !clazz.isPrimitive()){     
                System.out.println(clazz.getName());
                fail(clazz.getName() + " is not serializable");
            }            
            for (Field field : clazz.getDeclaredFields()){
                Set<Class<?>> types = new HashSet<Class<?>>(3);
                types.add(field.getType());
                if (field.getType().getSuperclass() != null){
                    types.add(field.getType().getSuperclass());    
                }                
                if (field.getType().getComponentType() != null){
                    types.add(field.getType().getComponentType());
                }
                if (Collection.class.isAssignableFrom(field.getType())){
                    types.add(ReflectionUtils.getTypeParameter(field.getGenericType(), 0)); 
                }else if (Map.class.isAssignableFrom(field.getType())){
                    types.add(ReflectionUtils.getTypeParameter(field.getGenericType(), 0));
                    types.add(ReflectionUtils.getTypeParameter(field.getGenericType(), 1));
                }
                types.removeAll(checked);
                classes.addAll(types);
            }
        }
    }
    
}
