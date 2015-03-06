package com.mysema.testutil;

import java.io.*;

public final class Serialization {

    private Serialization() { }

    public static <T> T serialize(T obj) {
        try {
            // serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(obj);
            out.close();

            // deserialize
            ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bain);
            T rv = (T) in.readObject();
            in.close();
            return rv;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
