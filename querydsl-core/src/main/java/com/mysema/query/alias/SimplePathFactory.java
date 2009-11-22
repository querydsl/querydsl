/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;
import org.apache.commons.lang.StringUtils;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PList;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadata;

/**
 * SimplePathFactory is a PathFactory implementation for the creation of Path
 * instances
 * 
 * @author tiwe
 * @version $Id$
 */
class SimplePathFactory implements PathFactory {

    private static class PathFactory<K, V> extends LazyMap<K, V> {

        private static final long serialVersionUID = -2443097467085594792L;

        protected PathFactory(Transformer<K, V> transformer) {
            super(new WeakHashMap<K, V>(), transformer);
        }

    }

    private final PString str = new PString(PathMetadata.forVariable("str"));

    private final PBoolean btrue = new PBoolean(md()), bfalse = new PBoolean(md());

    private long counter = 0;

    private final Map<Collection<?>, PCollection<?>> ecToPath = new PathFactory<Collection<?>, PCollection<?>>(
            new Transformer<Collection<?>, PCollection<?>>() {
                @SuppressWarnings("unchecked")
                public PCollection<?> transform(Collection<?> arg) {
                    if (!arg.isEmpty()) {
                        Class<?> cl = ((Collection) arg).iterator().next().getClass();
                        return new PCollection(cl, cl.getSimpleName(), md());
                    } else {
                        return new PCollection(Object.class, "Object", md());
                    }
                }
            });

    private final Map<List<?>, PList<?,?>> elToPath = new PathFactory<List<?>, PList<?,?>>(
            new Transformer<List<?>, PList<?,?>>() {
                @SuppressWarnings({ "unchecked", "serial" })
                public PList<?,?> transform(List<?> arg) {
                    final Class<?> cl = arg.isEmpty() ?  Object.class : arg.get(0).getClass();
                    return new PList<Object,PEntity<Object>>(Object.class, null, md()){                        
                        @Override
                        public PEntity get(Expr<Integer> index) {
                            return new PEntity(cl, cl.getSimpleName(), PathMetadata.forListAccess(this, index));
                        }
                        @Override
                        public PEntity get(int index) {
                            return new PEntity(cl, cl.getSimpleName(), PathMetadata.forListAccess(this, index));
                        }
                    };
                }
            });

    private final Map<Map<?, ?>, PMap<?, ?, ?>> emToPath = new PathFactory<Map<?, ?>, PMap<?, ?, ?>>(
            new Transformer<Map<?, ?>, PMap<?, ?, ?>>() {
                @SuppressWarnings("unchecked")
                public PMap<?, ?, ?> transform(Map<?, ?> arg) {
                    if (!arg.isEmpty()) {
                        Map.Entry entry = arg.entrySet().iterator().next();
                        final Class<Object> keyType = (Class)entry.getKey().getClass();
                        final Class<Object> valueType = (Class)entry.getValue().getClass();
                        return new PMap<Object,Object,PEntity<Object>>(keyType, valueType, null, md()){
                            @Override
                            public PEntity get(Expr<Object> key) {
                                return new PEntity(valueType, valueType.getSimpleName(), 
                                        PathMetadata.forMapAccess(this, key));
                            }
                            @Override
                            public PEntity get(Object key) {
                                return new PEntity(valueType, valueType.getSimpleName(), 
                                        PathMetadata.forMapAccess(this, key));
                            }
                        };
                    } else {
                        return new PMap<Object,Object,PEntity<Object>>(Object.class, Object.class, null, md()){
                            @Override
                            public PEntity get(Expr<Object> key) {
                                return new PEntity(Object.class, Object.class.getSimpleName(), 
                                        PathMetadata.forMapAccess(this, key));
                            }
                            @Override
                            public PEntity get(Object key) {
                                return new PEntity(Object.class, Object.class.getSimpleName(), 
                                        PathMetadata.forMapAccess(this, key));
                            }
                        };
                    }
                }

            });

    private final Map<Object, PComparable<?>> comToPath = new PathFactory<Object, PComparable<?>>(
            new Transformer<Object, PComparable<?>>() {
                @SuppressWarnings("unchecked")
                public PComparable<?> transform(Object arg) {
                    return new PComparable(arg.getClass(), md());
                }
            });

    private final Map<Object, PNumber<?>> numToPath = new PathFactory<Object, PNumber<?>>(
            new Transformer<Object, PNumber<?>>() {
                @SuppressWarnings("unchecked")
                public PNumber<?> transform(Object arg) {
                    return new PNumber(arg.getClass(), md());
                }
            });
    
    private final Map<Object, PDate<?>> dateToPath = new PathFactory<Object, PDate<?>>(
            new Transformer<Object, PDate<?>>() {
                @SuppressWarnings("unchecked")
                public PDate<?> transform(Object arg) {
                    return new PDate(arg.getClass(), md());
                }
            });
    
    private final Map<Object, PDateTime<?>> dateTimeToPath = new PathFactory<Object, PDateTime<?>>(
            new Transformer<Object, PDateTime<?>>() {
                @SuppressWarnings("unchecked")
                public PDateTime<?> transform(Object arg) {
                    return new PDateTime(arg.getClass(), md());
                }
            });
    
    private final Map<Object, PTime<?>> timeToPath = new PathFactory<Object, PTime<?>>(
            new Transformer<Object, PTime<?>>() {
                @SuppressWarnings("unchecked")
                public PTime<?> transform(Object arg) {
                    return new PTime(arg.getClass(), md());
                }
            });


    private final Map<Object, PEntity<?>> entityToPath = new PathFactory<Object, PEntity<?>>(
            new Transformer<Object, PEntity<?>>() {
                @SuppressWarnings("unchecked")
                public PEntity<?> transform(Object arg) {
                    return new PEntity(arg.getClass(), arg.getClass()
                            .getSimpleName(), md());
                }
            });

    private final Map<String, PString> strToPath = new PathFactory<String, PString>(
            new Transformer<String, PString>() {
                public PString transform(String str) {
                    return new PString(md());
                }
            });

    public <D> Expr<D> createAny(D arg) {
        throw new UnsupportedOperationException();
    }

    public PBoolean createBoolean(Boolean arg) {
        return arg.booleanValue() ? btrue : bfalse;
    }

    @SuppressWarnings("unchecked")
    public <D extends Comparable<?>> PComparable<D> createComparable(D arg) {
        return (PComparable<D>) comToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PDate<D> createDate(D arg) {
        return (PDate<D>) dateToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PDateTime<D> createDateTime(D arg) {
        return (PDateTime<D>) dateTimeToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <D> PEntity<D> createEntity(D arg) {
        return (PEntity<D>) entityToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <D> PCollection<D> createEntityCollection(Collection<D> arg) {
        return (PCollection<D>) ecToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <K, V> PMap<K, V, ?> createMap(Map<K, V> arg) {
        return (PMap<K, V, ?>) emToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <D extends Number & Comparable<?>> PNumber<D> createNumber(D arg) {
        return (PNumber<D>) numToPath.get(arg);
    }

    public PString createString(String arg) {
        return StringUtils.isEmpty(arg) ? str : strToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PTime<D> createTime(D arg) {
        return (PTime<D>) timeToPath.get(arg);
    }

    private PathMetadata<String> md() {
        return PathMetadata.forVariable("v" + String.valueOf(++counter));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D> PList<D,?> createList(List<D> arg) {
        return (PList<D,?>) elToPath.get(arg);
    }

}
