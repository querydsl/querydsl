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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SimpleSerializerConfig;

/**
 * @author tiwe
 *
 */
public class HibernateDomainExporter {

    private static final Logger logger = LoggerFactory.getLogger(HibernateDomainExporter.class);
    
    private final XMLInputFactory inFactory = XMLInputFactory.newInstance();
    
    private final String namePrefix;
    
    private final File targetFolder;
    
    private final List<EntityType> entityTypes = new ArrayList<EntityType>();
    
    private final List<File> hibernateXml;
    
    public HibernateDomainExporter(String namePrefix, File targetFolder, File... hibernateXml){
        this.namePrefix = namePrefix;
        this.targetFolder = targetFolder;
        this.hibernateXml = Arrays.asList(hibernateXml);
    }
    
    public void execute() throws FileNotFoundException, XMLStreamException{
        for (File xmlFile : hibernateXml){
            
            InputStream in = new FileInputStream(xmlFile);
            XMLStreamReader reader = inFactory.createXMLStreamReader(in);
            try{
                EntityType entityType = null;
                while (true) {
                    int event = reader.next();
                    if (event == START_ELEMENT) {
                        String name = reader.getLocalName();
                        if (name.equals("class")){
                            String className = reader.getAttributeValue(null, "name");
                            String packageName = className.substring(0, className.lastIndexOf("."));
                            String simpleName = className.substring(packageName.length());
                            entityType = new EntityType(namePrefix, new SimpleType(className, packageName, simpleName));
                            
                        }else if (name.equals("property")){
                            String propertyName = reader.getAttributeValue(null, "name");
                            // TODO
                        }
                    }else if (event == END_ELEMENT){    
                        String name = reader.getLocalName();
                        if (name.equals("class")){
                            entityTypes.add(entityType);
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
        
        // serialize entity types
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
