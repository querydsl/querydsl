package com.querydsl.core.testutil;

import java.io.*;

public final class Serialization {

    private Serialization() { }

    @SuppressWarnings("unchecked")
    public static <T> T serialize(T obj) {
        try {
            // serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(obj);
            out.close();

            // deserialize
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            T rv = (T) in.readObject();
            in.close();
            return rv;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
