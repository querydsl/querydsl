/*
 * Copyright (C) 2008 Eric Bottard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.alias;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Quick and dirty mix of {@link WeakHashMap} and {@link IdentityHashMap}. This
 * class does not implement {@link Map} <i>per se</i> but provides the methods
 * we need.
 */
/* default */class WeakIdentityHashMap<K, V> {

    private Map<WeakReference<K>, V> map = new HashMap<WeakReference<K>, V>();

    private ReferenceQueue<K> referenceQueue = new ReferenceQueue<K>();

    private void expunge() {
        Reference<? extends K> ref;
        while ((ref = referenceQueue.poll()) != null) {
            map.remove(ref);
        }
    }

    public V get(K key) {
        expunge();
        WeakReference<K> keyref = makeReference(key);
        return map.get(keyref);
    }

    private WeakReference<K> makeReference(K referent) {
        return new IdentityWeakReference<K>(referent);
    }

    private WeakReference<K> makeReference(K referent, ReferenceQueue<K> q) {
        return new IdentityWeakReference<K>(referent, q);
    }

    public V put(K key, V value) {
        expunge();
        if (key == null) {
            throw new NullPointerException("Null key");
        }
        WeakReference<K> keyref = makeReference(key, referenceQueue);
        return map.put(keyref, value);
    }

    public V remove(K key) {
        expunge();
        WeakReference<K> keyref = makeReference(key);
        return map.remove(keyref);
    }

    /**
     * Considers that two objects are equal when they both reference the same
     * (with <tt>==</tt> semantics) referent.
     */
    private static class IdentityWeakReference<T> extends WeakReference<T> {
        private final int hashCode;

        IdentityWeakReference(T o) {
            this(o, null);
        }

        IdentityWeakReference(T o, ReferenceQueue<T> q) {
            super(o, q);
            this.hashCode = (o == null) ? 0 : System.identityHashCode(o);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IdentityWeakReference)) {
                return false;
            }
            IdentityWeakReference<T> wr = (IdentityWeakReference<T>) o;
            T got = get();
            return (got != null && got == wr.get());
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
