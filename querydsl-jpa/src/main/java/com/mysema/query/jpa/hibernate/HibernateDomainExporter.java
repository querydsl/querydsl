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
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.codegen.*;
import com.sun.xml.internal.ws.util.StringUtils;

/**
 * @author tiwe
 *
 */
public class HibernateDomainExporter {
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateDomainExporter.class);
    
    private static final Set<String> propertyFields = new HashSet<String>(Arrays.asList(
            "property","dynamic-component","properties","any","map","set","list","bag","idbag","array","primitive-array"));

    private static final Set<String> entityFields = new HashSet<String>(Arrays.asList("many-to-one","one-to-one"));
    
    private static final Set<String> embeddableFields = new HashSet<String>(Arrays.asList("component"));
    
    private final XMLInputFactory inFactory = XMLInputFactory.newInstance();
    
    private final String namePrefix;
    
    private final File targetFolder;

    private final Map<String,EntityType> allTypes = new HashMap<String,EntityType>();
    
    private final Map<String,EntityType> entityTypes = new HashMap<String,EntityType>();
    
    private final Map<String,EntityType> embeddableTypes = new HashMap<String,EntityType>();
    
    private final Map<String,EntityType> superTypes = new HashMap<String,EntityType>();
    
    private final Set<EntityType> serialized = new HashSet<EntityType>();
    
    private final List<File> hibernateXml;
    
    private final TypeMappings typeMappings = new TypeMappings();

    private final Serializer embeddableSerializer = new EmbeddableSerializer(typeMappings, Constants.keywords);

    private final Serializer entitySerializer = new EntitySerializer(typeMappings, Constants.keywords);

    private final Serializer supertypeSerializer = new SupertypeSerializer(typeMappings, Constants.keywords);

    private final TypeFactory typeFactory = new TypeFactory();
    
    private final SerializerConfig serializerConfig;
    
    public HibernateDomainExporter(String namePrefix, File targetFolder, File... hibernateXml){
        this(namePrefix, targetFolder, SimpleSerializerConfig.DEFAULT, hibernateXml);
    }
    
    public HibernateDomainExporter(String namePrefix, File targetFolder, SerializerConfig serializerConfig, File... hibernateXml){
        this.namePrefix = namePrefix;
        this.targetFolder = targetFolder;
        this.serializerConfig = serializerConfig;
        this.hibernateXml = Arrays.asList(hibernateXml);
        typeFactory.setUnknownAsEntity(true);
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
     
        // merge supertype fields into subtypes
        Set<EntityType> handled = new HashSet<EntityType>();
        for (EntityType type : superTypes.values()){
            addSupertypeFields(type, allTypes, handled);
        }
        for (EntityType type : entityTypes.values()){
            addSupertypeFields(type, allTypes, handled);
        }
        for (EntityType type : embeddableTypes.values()){
            addSupertypeFields(type, allTypes, handled);
        }
        
        // serialize them
        serialize(superTypes, supertypeSerializer);
        serialize(embeddableTypes, embeddableSerializer);
        serialize(entityTypes, entitySerializer);
    }
    
    private void addSupertypeFields(EntityType model, Map<String, EntityType> superTypes, Set<EntityType> handled) {
        if (handled.add(model)){
            for (Supertype supertype : model.getSuperTypes()){
                EntityType entityType = superTypes.get(supertype.getType().getFullName());
                if (entityType != null){
                    addSupertypeFields(entityType, superTypes, handled);
                    supertype.setEntityType(entityType);
                    model.include(supertype);
                }
            }
        }        
    }
    

    private void collectTypes() throws IOException, XMLStreamException, ClassNotFoundException, SecurityException, NoSuchMethodException {
        for (File xmlFile : hibernateXml){            
            InputStream in = new FileInputStream(xmlFile);
            XMLStreamReader reader = inFactory.createXMLStreamReader(in);
            try{
                String packageName = null;
                Stack<EntityType> types = new Stack<EntityType>();
                Stack<Class<?>> classes = new Stack<Class<?>>();
                while (true) {
                    int event = reader.next();
                    if (event == START_ELEMENT) {
                        String name = reader.getLocalName();
                        if (name.equals("hibernate-mapping")){
                            packageName = reader.getAttributeValue(null, "package");
                        
                        }else if (name.endsWith("class")){
                            String className = reader.getAttributeValue(null, "name");
                            classes.push(Class.forName(packageName == null ? className : packageName + "." + className));
                            if (name.equals("mapped-superclass")){
                                types.push(createSuperType(classes.peek()));
                            }else{
                                types.push(createEntityType(classes.peek()));    
                            }                            
                            
                        }else if (propertyFields.contains(name)){
                            String propertyName = reader.getAttributeValue(null, "name");
                            Type propertyType = getType(classes.peek(), propertyName);
                            Map<Class<?>,Annotation> annotations = getAnnotations(classes.peek(), propertyName);
                            Property property = createProperty(types.peek(), propertyName, propertyType, annotations);
                            types.peek().addProperty(property);
                            
                        }else if (entityFields.contains(name)){
                            String propertyName = reader.getAttributeValue(null, "name");                            
                            Type propertyType = createEntityType(Class.forName(getType(classes.peek(), propertyName).getFullName()));
                            Map<Class<?>,Annotation> annotations = getAnnotations(classes.peek(), propertyName);
                            Property property = createProperty(types.peek(), propertyName, propertyType, annotations);
                            types.peek().addProperty(property);
                            
                        }else if (embeddableFields.contains(name)){
                            String propertyName = reader.getAttributeValue(null, "name");      
                            Class<?> clazz = Class.forName(getType(classes.peek(), propertyName).getFullName());
                            EntityType propertyType = createEmbeddableType(clazz);
                            Map<Class<?>,Annotation> annotations = getAnnotations(classes.peek(), propertyName);
                            Property property = createProperty(types.peek(), propertyName, propertyType, annotations);
                            types.peek().addProperty(property);
                            types.push(propertyType);
                            classes.push(clazz);
                        }
                        
                    }else if (event == END_ELEMENT){    
                        String name = reader.getLocalName();
                        if (name.endsWith("class")  || embeddableFields.contains(name)){
                            types.pop();
                            classes.pop();
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

    private Property createProperty(EntityType entityType, String propertyName, Type propertyType, Map<Class<?>, Annotation> annotations) {
        String[] inits = new String[0];
        if (annotations.containsKey(QueryInit.class)){
            inits = ((QueryInit)annotations.get(QueryInit.class)).value();
        }
        if (annotations.containsKey(QueryType.class)){
            propertyType = propertyType.as(((QueryType)annotations.get(QueryType.class)).value().getCategory());
        }
        Property property = new Property(entityType, propertyName, propertyType, inits);
        return property;
    }

    private EntityType createEntityType(Class<?> cl) {
        return createEntityType(cl, entityTypes);
    }
    
    private EntityType createEmbeddableType(Class<?> cl) {
        return createEntityType(cl, embeddableTypes);
    }

    private EntityType createEntityType(Class<?> cl,  Map<String,EntityType> types) {
        if (types.containsKey(cl.getName())){
            return types.get(cl.getName());
        }else{
            EntityType type = new EntityType(namePrefix, new ClassType(TypeCategory.ENTITY, cl));
            if (!cl.getSuperclass().equals(Object.class)){
                type.addSupertype(new Supertype(new ClassType(cl.getSuperclass())));
            }
            types.put(cl.getName(), type);
            allTypes.put(cl.getName(), type);
            return type;
        }
    }

    private EntityType createSuperType(Class<?> cl) {
        return createEntityType(cl, superTypes);
    }
    

    private Type getType(Class<?> cl, String propertyName) throws NoSuchMethodException {
        try {
            Field field = cl.getDeclaredField(propertyName);
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
    
    private Map<Class<?>,Annotation> getAnnotations(Class<?> cl, String propertyName) throws NoSuchMethodException {
        try {
            Field field = cl.getDeclaredField(propertyName);
            return getAnnotations(field.getAnnotations());
        } catch (NoSuchFieldException e) {
            try{
                Method method = cl.getMethod("get"+StringUtils.capitalize(propertyName));
                return getAnnotations(method.getAnnotations());    
            }catch(NoSuchMethodException e1){
                Method method = cl.getMethod("is"+StringUtils.capitalize(propertyName));
                return getAnnotations(method.getAnnotations());
            }
        }
    }

    private Map<Class<?>, Annotation> getAnnotations(Annotation[] a) {
        Map<Class<?>,Annotation> annotations = new HashMap<Class<?>,Annotation>();
        for (Annotation annotation : a){
            annotations.put(annotation.annotationType(), annotation);
        }
        return annotations;
    }

    private void serialize(Map<String,EntityType> types, Serializer serializer) throws IOException {
        for (EntityType entityType : types.values()){
            if (serialized.add(entityType)){
                Type type = typeMappings.getPathType(entityType, entityType, true);
                String packageName = entityType.getPackageName();
                String className = packageName.length() > 0 ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();
                write(serializer, className.replace('.', '/') + ".java", entityType);    
            }            
        }   
    }
    
    private void write(Serializer serializer, String path, EntityType type) throws IOException {
        File targetFile = new File(targetFolder, path);
        Writer w = writerFor(targetFile);
        try{
            CodeWriter writer = new JavaWriter(w);
            serializer.serialize(type, serializerConfig, writer);
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
