/*
 * Copyright 2014, Timo Westkämper
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
package com.querydsl.core;

import static com.google.common.base.StandardSystemProperty.JAVA_SPECIFICATION_VERSION;

/**
 * The different Java™ specification versions.
 *
 * @author Shredder121
 */
public enum JavaSpecVersion {

    JAVA6("1.6"),
    JAVA7("1.7"),
    JAVA8("1.8");

    public static final JavaSpecVersion CURRENT
            = getByVersionNumber(JAVA_SPECIFICATION_VERSION.value());

    private final String versionNumber;

    private JavaSpecVersion(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    private boolean isVersion(String versionNumber) {
        return this.versionNumber.equals(versionNumber);
    }

    private static JavaSpecVersion getByVersionNumber(String versionNumber) {
        for (JavaSpecVersion version : values()) {
            if (version.isVersion(versionNumber)) {
                return version;
            }
        }
        throw new IllegalArgumentException("versionNumber " + versionNumber + " not found");
    }
}
