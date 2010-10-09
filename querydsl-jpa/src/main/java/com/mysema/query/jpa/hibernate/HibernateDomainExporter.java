package com.mysema.query.jpa.hibernate;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.query.QueryException;
import com.mysema.query.codegen.EmbeddableSerializer;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.codegen.SupertypeSerializer;
import com.mysema.query.codegen.TypeFactory;
import com.mysema.query.codegen.TypeMappings;
import com.sun.xml.internal.ws.util.StringUtils;

/**
 * @author tiwe
 *
 */
public class HibernateDomainExporter {

    private static final Logger logger = LoggerFactory.getLogger(HibernateDomainExporter.class);
    
    private static final List<String> propertyFields = Arrays.asList("property","many-to-one","one-to-one","component","dynamic-component","properties","any","map","set","list","bag","idbag","array","primitive-array");
    
    private final XMLInputFactory inFactory = XMLInputFactory.newInstance();
    
    private final String namePrefix;
    
    private final File targetFolder;
    
    private final List<EntityType> entityTypes = new ArrayList<EntityType>();
    
    private final List<EntityType> embeddableTypes = new ArrayList<EntityType>();
    
    private final List<EntityType> superTypes = new ArrayList<EntityType>();
    
    private final List<File> hibernateXml;
    
    private final TypeMappings typeMappings = new TypeMappings();

    private final Serializer embeddableSerializer = new EmbeddableSerializer(typeMappings, Constants.keywords);

    private final Serializer entitySerializer = new EntitySerializer(typeMappings, Constants.keywords);

    private final Serializer supertypeSerializer = new SupertypeSerializer(typeMappings, Constants.keywords);

    private final TypeFactory typeFactory = new TypeFactory();
    
    public HibernateDomainExporter(String namePrefix, File targetFolder, File... hibernateXml){
        this.namePrefix = namePrefix;
        this.targetFolder = targetFolder;
        this.hibernateXml = Arrays.asList(hibernateXml);
    }
    
    public void execute() throws IOException {        
        // collect types
        try {
            collectTypes();
        } catch (SecurityException e) {
            throw new QueryException(e);
        } catch (XMLStreamException e) {
            throw new QueryException(e);
        } catch (ClassNotFoundException e) {
            throw new QueryException(e);
        } catch (NoSuchMethodException e) {
            throw new QueryException(e);
        }
     
        // TODO : merge supertype fields into subtypes
        
        // serialize them
        serialize(superTypes, supertypeSerializer);
        serialize(embeddableTypes, embeddableSerializer);
        serialize(entityTypes, entitySerializer);
    }

    private void serialize(List<EntityType> types, Serializer serializer) throws IOException {
        for (EntityType entityType : types){
            Type type = typeMappings.getPathType(entityType, entityType, true);
            String packageName = entityType.getPackageName();
            String className = packageName.length() > 0 ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();
            write(serializer, className.replace('.', '/') + ".java", entityType);
        }   
    }

    private void collectTypes() throws IOException, XMLStreamException, ClassNotFoundException, SecurityException, NoSuchMethodException {
        for (File xmlFile : hibernateXml){
            
            InputStream in = new FileInputStream(xmlFile);
            XMLStreamReader reader = inFactory.createXMLStreamReader(in);
            try{
                Stack<EntityType> types = new Stack<EntityType>();
                Class<?> cl = null;
                while (true) {
                    int event = reader.next();
                    if (event == START_ELEMENT) {
                        String name = reader.getLocalName();
                        if (name.equals("class") || name.endsWith("subclass")){
                            cl = Class.forName(reader.getAttributeValue(null, "name"));
                            Type simpleType = new ClassType(TypeCategory.ENTITY, cl);
                            types.push(new EntityType(namePrefix, simpleType));
                            
                        }else if (propertyFields.contains(name)){
                            String propertyName = reader.getAttributeValue(null, "name");
                            Type propertyType = getType(cl, propertyName);
                            Property property = new Property(types.peek(), propertyName, propertyType);
                            types.peek().addProperty(property);
                        } 
                    }else if (event == END_ELEMENT){    
                        String name = reader.getLocalName();
                        if (name.equals("class") || name.endsWith("subclass")){
                            entityTypes.add(types.peek());
                            types.pop();
                        }
                    } else if (event == END_DOCUMENT) {
                        break;
                    }
                }                
            }finally{
                IOUtils.closeQuietly(in);
                reader.close();
            }
        }
    }

    private Type getType(Class<?> cl, String propertyName) throws NoSuchMethodException {
        try {
            Field field = cl.getField(propertyName);
            return typeFactory.create(field.getType(), field.getGenericType());
        } catch (NoSuchFieldException e) {
            try{
                Method method = cl.getMethod("get"+StringUtils.capitalize(propertyName));
                return typeFactory.create(method.getReturnType(), method.getGenericReturnType());    
            }catch(NoSuchMethodException e1){
                Method method = cl.getMethod("is"+StringUtils.capitalize(propertyName));
                return typeFactory.create(method.getReturnType(), method.getGenericReturnType());
            }
        }
    }
    
    private void write(Serializer serializer, String path, EntityType type) throws IOException {
        File targetFile = new File(targetFolder, path);
        Writer w = writerFor(targetFile);
        try{
            CodeWriter writer = new JavaWriter(w);
            serializer.serialize(type, SimpleSerializerConfig.DEFAULT, writer);
        }finally{
            w.close();
        }
    }
    
    private Writer writerFor(File file) {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            logger.error("Folder " + file.getParent() + " could not be created");
        }
        try {
            return new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
}
