/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

/**
 * JoinType defines the supported join types
 * 
 * @author tiwe
 * @version $Id$
 */
public enum JoinType {
    DEFAULT, INNERJOIN {
        public String toString() {
            return "INNER JOIN";
        }
    },
    JOIN, LEFTJOIN {
        public String toString() {
            return "LEFT JOIN";
        }
    },
    FULLJOIN {
        public String toString() {
            return "FULL JOIN";
        }
    }
}