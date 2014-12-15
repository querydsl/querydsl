package com.mysema.query.elasticsearch.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.mysema.query.elasticsearch.ElasticsearchQuery;
import com.mysema.query.elasticsearch.ElasticsearchSerializer;
import com.mysema.query.types.EntityPath;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * JacksonElasticsearchQueries is a factory to provide ElasticsearchQuery basic implementation.
 *
 * @author kevinleturc
 */
public class JacksonElasticsearchQueries {

    private final Client client;

    /**
     * Default constructor.
     *
     * @param client The elasticsearch client.
     */
    public JacksonElasticsearchQueries(Client client) {
        this.client = client;
    }

    public <K> ElasticsearchQuery<K> query(EntityPath<K> entityPath, String index, String type) {
        return query(entityPath, index, type, new ElasticsearchSerializer());
    }

    public <K> ElasticsearchQuery<K> query(EntityPath<K> entityPath, String index, String type, ElasticsearchSerializer serializer) {
        return query(entityPath, index, type, serializer, defaultTransformer(entityPath));
    }

    public <K> ElasticsearchQuery<K> query(EntityPath<K> entityPath, String index, String type, Function<SearchHit, K> transformer) {
        return query(entityPath, index, type, new ElasticsearchSerializer(), transformer);
    }

    public <K> ElasticsearchQuery<K> query(EntityPath<K> entityPath, final String index, final String type, ElasticsearchSerializer serializer, Function<SearchHit, K> transformer) {
        return new ElasticsearchQuery<K>(client, transformer, serializer, entityPath) {

            /**
             * {@inheritDoc}
             */
            @Override
            public String getIndex(Class<? extends K> entityType) {
                return index;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String getType(Class<? extends K> entityType) {
                return type;
            }

        };
    }

    /**
     * Returns the default transformer.
     *
     * @param entityPath The entity path.
     * @param <K> The entity type.
     * @return The default transformer.
     */
    private <K> Function<SearchHit, K> defaultTransformer(final EntityPath<K> entityPath) {
        final ObjectMapper mapper = new ObjectMapper();
        return new Function<SearchHit, K>() {

            /**
             * {@inheritDoc}
             */
            @Nullable
            @Override
            public K apply(@Nullable SearchHit input) {
                try {
                    Class<? extends K> entityType = entityPath.getType();
                    K bean = mapper.readValue(input.getSourceAsString(), entityType);

                    Field idField = null;
                    Class<?> target = entityType;
                    while (idField == null && target != Object.class) {
                        for (Field field : target.getDeclaredFields()) {
                            if ("id".equals(field.getName())) {
                                idField = field;
                            }
                        }
                        target = target.getSuperclass();
                    }
                    if (idField != null) {
                        idField.setAccessible(true);
                        idField.set(bean, input.getId());
                    }

                    return bean;
                } catch (SecurityException se) {
                    throw new MappingException("Unable to lookup id field, may be use a custom transformer ?", se);
                } catch (IllegalAccessException e) {
                    throw new MappingException("Unable to set id value in id field, may be use a custom transformer ?", e);
                } catch (IOException e) {
                    throw new MappingException("Unable to read the Elasticsearch response.", e);
                }
            }
        };
    }
}
