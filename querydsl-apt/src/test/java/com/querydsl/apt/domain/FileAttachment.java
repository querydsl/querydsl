package com.querydsl.apt.domain;

import java.io.File;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
@Deprecated
public class FileAttachment {

    @Transient
    Object model;
    @Transient
    String name;
    @Transient
    File f;
    public String filename;

    public FileAttachment() {
    }

    FileAttachment(Object model, String name) {
        this.model = model;
        this.name = name;
    }

    public File get() {
        return f;
    }

    public void set(File file) {
        f = file;
    }

    public boolean isSet() {
        return f != null || get() != null;
    }

    public static File getStore() {
        return null;
    }

    @Deprecated
    public boolean exists() {
        return isSet();
    }

    @Deprecated
    public long length() {
        return get().length();
    }

}