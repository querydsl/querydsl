package com.mysema.query.alias;

import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;

public class PathFactoryTest {

    private PathFactory pathFactory = new DefaultPathFactory();
    
    private PathMetadata<?> metadata = PathMetadataFactory.forVariable("var");
    
    @Test
    public void CreateArrayPath() {
        Path<String[]> path = pathFactory.createArrayPath(String[].class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateEntityPath() {
        Path<Object> path = pathFactory.createEntityPath(Object.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateSimplePath() {
        Path<Object> path = pathFactory.createSimplePath(Object.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateComparablePath() {
        Path<String> path = pathFactory.createComparablePath(String.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateEnumPath() {
        Path<PropertyType> path = pathFactory.createEnumPath(PropertyType.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateDatePath() {
        Path<Date> path = pathFactory.createDatePath(Date.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateTimePath() {
        Path<Time> path = pathFactory.createTimePath(Time.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateDateTimePath() {
        Path<Timestamp> path = pathFactory.createDateTimePath(Timestamp.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateNumberPath() {
        Path<Integer> path = pathFactory.createNumberPath(Integer.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateBooleanPath() {
        Path<Boolean> path = pathFactory.createBooleanPath(metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateStringPath() {
        Path<String> path = pathFactory.createStringPath(metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateListPath() {
        Path<List<Timestamp>> path = pathFactory.createListPath(Timestamp.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateSetPath() {
        Path<Set<Timestamp>> path = pathFactory.createSetPath(Timestamp.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateCollectionPath() {
        Path<Collection<Timestamp>> path = pathFactory.createCollectionPath(Timestamp.class, metadata);
        assertNotNull(path);
    }

    @Test
    public void CreateMapPath() {
        Path<Map<String,Timestamp>> path = pathFactory.createMapPath(String.class, Timestamp.class, metadata);
        assertNotNull(path);
    }

}
