package com.mysema.query.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class ReservedNames {

    public boolean isNew() {
        return false;
    }

    public String getPackage() {
        return "";
    }

    public int getProtected() {
        return 1;
    }

    public List<ReservedNames> getIf() {
        return null;
    }

    public Set<ReservedNames> getElse() {
        return null;
    }

    public List<String> getTry() {
        return null;
    }

    public Set<Integer> getCatch() {
        return null;
    }

    public Map<String, ReservedNames> getWhile() {
        return null;
    }

    public Map<String, String> getFor() {
        return null;
    }

    public ReservedNames getExtends() {
        return null;
    }

}
