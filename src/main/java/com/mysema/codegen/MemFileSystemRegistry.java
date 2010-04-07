/*
 * Copyright (c) 2010 Mysema Ltd.
 * 
 * base on code from https://hickory.dev.java.net/
 * 
 */

package com.mysema.codegen;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;
import javax.tools.JavaFileManager;

/**
 * @author tiwe
 *
 */
public class MemFileSystemRegistry {
    
    public static final MemFileSystemRegistry DEFAULT = new MemFileSystemRegistry();
    
    private final Map<JavaFileManager,String> jfm2prefix = new WeakHashMap<JavaFileManager,String>();
    
    private Map<String,WeakReference<JavaFileManager>> prefix2jfm = new WeakHashMap<String,WeakReference<JavaFileManager>>();
    
    private final String protocolName;
    
    private int sequence = 0;
    
    private MemFileSystemRegistry() {
        String pkgName = MemFileSystemRegistry.class.getPackage().getName();
        protocolName = pkgName.substring(pkgName.lastIndexOf(".") + 1);
        String pkgs = System.getProperty("java.protocol.handler.pkgs");
        String parentPackage = pkgName.substring(0,pkgName.lastIndexOf("."));
        pkgs = pkgs == null ? parentPackage : pkgs + "|" + parentPackage;
        System.setProperty("java.protocol.handler.pkgs",pkgs);
    }
    
    public JavaFileManager getFileSystem(URL url) {
        String prefix = url.getProtocol() + "://" + url.getHost() + "/";
        if(prefix2jfm.containsKey(prefix)) {
            return prefix2jfm.get(prefix).get();
        } else {
            return null;
        }
    }
    
    public String getUrlPrefix(JavaFileManager jfm) {
        if(jfm2prefix.containsKey(jfm)) {
            return jfm2prefix.get(jfm);
        } else {
            String result = protocolName + "://jfm" + (sequence++) + "/";
            jfm2prefix.put(jfm,result);
            prefix2jfm.put(result, new WeakReference<JavaFileManager>(jfm));
            return result;
        }
    }
    
}
