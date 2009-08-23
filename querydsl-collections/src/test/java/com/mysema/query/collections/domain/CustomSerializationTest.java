package com.mysema.query.collections.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.Serializers;

import freemarker.template.TemplateException;

public class CustomSerializationTest {

    @Test
    public void test() throws IOException, TemplateException{
        List<ClassModel> classModels = new ArrayList<ClassModel>();
        for (Class<?> cl : Arrays.<Class<?>>asList(Animal.class, Cat.class, Color.class)){
            classModels.add(ClassModel.create(cl));
        }
        Serializers.DOMAIN.serialize("target/generated", classModels);
    }
    
}
