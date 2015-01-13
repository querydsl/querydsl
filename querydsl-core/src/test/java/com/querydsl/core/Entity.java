package com.querydsl.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Entity{

    int getNum();

    String getStr();

    boolean isBool();

    List<String> getList();

    Set<String> getSet();

    Map<String, String> getMap();

    java.util.Date getDateTime();

    java.sql.Date getDate();

    java.sql.Time getTime();

    String[] getArray();
}