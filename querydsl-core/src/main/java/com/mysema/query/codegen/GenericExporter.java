package com.mysema.query.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.support.ClassUtils;
import com.mysema.query.QueryException;
import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.annotations.QueryType;

/**
 * @author tiwe
 *
 */
public class GenericExporter {

    private static final Pattern jarUrlSeparator = Pattern.compile("!");

    private Class<? extends Annotation> entityAnnotation = QueryEntity.class;

    private Class<? extends Annotation> supertypeAnnotation = QuerySupertype.class;

    private Class<? extends Annotation> embeddableAnnotation = QueryEmbeddable.class;

    private Class<? extends Annotation> embeddedAnnotation = QueryEmbedded.class;

    private final Map<String, EntityType> allTypes = new HashMap<String, EntityType>();

    private final Map<Class<?>, EntityType> entityTypes = new HashMap<Class<?>, EntityType>();

    private final Map<Class<?>, EntityType> superTypes = new HashMap<Class<?>, EntityType>();

    private final Map<Class<?>, EntityType> embeddableTypes = new HashMap<Class<?>, EntityType>();

    private final CodegenModule codegenModule = new CodegenModule();

    private final SerializerConfig serializerConfig = SimpleSerializerConfig.DEFAULT;

    @Nullable
    private File targetFolder;

    @Nullable
    private TypeFactory typeFactory;

    @Nullable
    private TypeMappings typeMappings;

    @Nullable
    private QueryTypeFactory queryTypeFactory;

    @Nullable
    private Class<? extends Serializer> serializerClass;

    public void export(Package... packages){
        scanPackages(packages);

        typeMappings = codegenModule.get(TypeMappings.class);
        queryTypeFactory = codegenModule.get(QueryTypeFactory.class);
        typeFactory = new TypeFactory(entityAnnotation, supertypeAnnotation, embeddableAnnotation);

        // process supertypes
        for (Class<?> cl : superTypes.keySet()){
            superTypes.put(cl, createEntityType(cl, superTypes));
        }

        // process embeddables
        for (Class<?> cl : embeddableTypes.keySet()){
            embeddableTypes.put(cl, createEntityType(cl, embeddableTypes));
        }

        // process entities
        for (Class<?> cl : entityTypes.keySet()){
            entityTypes.put(cl, createEntityType(cl, entityTypes));
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

        try{
            Serializer supertypeSerializer, entitySerializer, embeddableSerializer;

            if (serializerClass != null) {
                Serializer serializer = codegenModule.get(serializerClass);
                supertypeSerializer = serializer;
                entitySerializer = serializer;
                embeddableSerializer = serializer;
            } else {
                supertypeSerializer = codegenModule.get(SupertypeSerializer.class);
                entitySerializer = codegenModule.get(EntitySerializer.class);
                embeddableSerializer = codegenModule.get(EmbeddableSerializer.class);
            }

            // serialize super types
            serialize(supertypeSerializer, superTypes);

            // serialze entity types
            serialize(entitySerializer, entityTypes);

            // serialize embeddables
            serialize(embeddableSerializer, embeddableTypes);

        } catch (IOException e) {
            throw new QueryException(e);
        }

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

    private EntityType createEntityType(Class<?> cl, Map<Class<?>, EntityType> types) {
        if (types.containsKey(cl.getName())){
            return types.get(cl.getName());
        }else{
            EntityType type = new EntityType(new ClassType(TypeCategory.ENTITY, cl));
            typeMappings.register(type, queryTypeFactory.create(type));
            if (!cl.getSuperclass().equals(Object.class)){
                type.addSupertype(new Supertype(new ClassType(cl.getSuperclass())));
            }

            for (Field field : cl.getDeclaredFields()){
                if (!Modifier.isStatic(field.getModifiers())){
                    Map<Class<?>, Annotation> annotations = new HashMap<Class<?>, Annotation>();
                    for (Annotation annotation : field.getAnnotations()){
                        annotations.put(annotation.annotationType(), annotation);
                    }
                    if (annotations.get(embeddedAnnotation) != null) {
                        // TODO : handle collection and map property here
                        embeddableTypes.put(field.getType(), createEntityType(field.getType(), embeddableTypes));
                    }
                    Type propertyType = types.get(field.getType());
                    if (propertyType == null){
                        propertyType = typeFactory.create(field.getType(), field.getGenericType());
                    }
                    Property property = createProperty(type, field.getName(), propertyType, annotations);
                    type.addProperty(property);
                }
            }

            allTypes.put(ClassUtils.getFullName(cl), type);
            return type;
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


    private void scanPackages(Package... packages){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (Package pkg : packages){
            try {
                for (Class<?> cl : scanPackage(classLoader, pkg)){
                    if (cl.getAnnotation(entityAnnotation) != null){
                        entityTypes.put(cl, null);
                    }else if (cl.getAnnotation(embeddableAnnotation) != null){
                        embeddableTypes.put(cl, null);
                    }else if (cl.getAnnotation(supertypeAnnotation) != null){
                        superTypes.put(cl, null);
                    }
                }
            } catch (IOException e) {
                throw new QueryException(e);
            } catch (ClassNotFoundException e) {
                throw new QueryException(e);
            }
        }
    }

    Set<Class<?>> scanPackage(ClassLoader classLoader, Package pkg) throws IOException, ClassNotFoundException {
        Enumeration<URL> urls = classLoader.getResources(pkg.getName().replace('.', '/'));
        Set<Class<?>> classes = new HashSet<Class<?>>();
        while (urls.hasMoreElements()){
            URL url = urls.nextElement();
            if (url.getProtocol().equals("jar")){
                String[] fileAndPath = jarUrlSeparator.split(url.getFile().substring(5));
                JarFile jarFile = new JarFile(fileAndPath[0]);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()){
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class") && entry.getName().startsWith(fileAndPath[1].substring(1))){
                        String className = entry.getName().substring(0, entry.getName().length()-6).replace('/', '.');
                        classes.add(Class.forName(className));

                    }
                }

            }else if (url.getProtocol().equals("file")){
                Deque<File> files = new ArrayDeque<File>();
                String packagePath;
                try {
                    packagePath = url.toURI().getPath();
                    files.add(new File(packagePath));
                } catch (URISyntaxException e) {
                    throw new IOException(e);
                }
                while (!files.isEmpty()){
                    File file = files.pop();
                    for (File child : file.listFiles()){
                        if (child.getName().endsWith(".class")){
                            String fileName = child.getName();
                            String className = pkg.getName() + "." + fileName.substring(0, fileName.length()-6);
                            classes.add(Class.forName(className));
                        }else if (child.isDirectory()){
                            files.add(child);
                        }
                    }
                }

            }else{
                throw new IllegalArgumentException("Illegal url : " + url);
            }
        }
        return classes;
    }

    private void serialize(Serializer serializer, Map<Class<?>, EntityType> types) throws IOException {
        for (EntityType entityType : types.values()){
            Type type = typeMappings.getPathType(entityType, entityType, true);
            String packageName = type.getPackageName();
            String className = packageName.length() > 0 ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();
            write(serializer, className.replace('.', '/') + ".java", entityType);
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
            System.err.println("Folder " + file.getParent() + " could not be created");
        }
        try {
            return new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void setEntityAnnotation(Class<? extends Annotation> entityAnnotation) {
        this.entityAnnotation = entityAnnotation;
    }

    public void setSupertypeAnnotation(
            Class<? extends Annotation> supertypeAnnotation) {
        this.supertypeAnnotation = supertypeAnnotation;
    }

    public void setEmbeddableAnnotation(
            Class<? extends Annotation> embeddableAnnotation) {
        this.embeddableAnnotation = embeddableAnnotation;
    }

    public void setEmbeddedAnnotation(Class<? extends Annotation> embeddedAnnotation) {
        this.embeddedAnnotation = embeddedAnnotation;
    }

    public void setTargetFolder(File targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void setSerializerClass(Class<? extends Serializer> serializerClass) {
        codegenModule.bind(serializerClass);
        this.serializerClass = serializerClass;
    }



}
