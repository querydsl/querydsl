/*
 * Copyright 2010, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.codegen.utils;

import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

/**
 * LocationAndKind defines a pair of Location and Kind
 * 
 * @author tiwe
 * 
 */
public class LocationAndKind {

    private final Kind kind;

    private final Location location;

    public LocationAndKind(Location location, Kind kind) {
        this.location = location;
        this.kind = kind;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof LocationAndKind) {
            LocationAndKind other = (LocationAndKind) obj;
            return location.equals(other.location) && kind.equals(other.kind);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return kind.hashCode() * 31 + location.hashCode();
    }

    @Override
    public String toString() {
        return kind.toString() + "@" + location.toString();
    }

}
