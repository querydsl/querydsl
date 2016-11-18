package com.querydsl.sql.codegen.support;

/**
 * Models a custom type for the AntMetaDataExporter
 */
public class CustomType {
  private String className;

  public CustomType() {
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String aClassName) {
    className = aClassName;
  }
}
