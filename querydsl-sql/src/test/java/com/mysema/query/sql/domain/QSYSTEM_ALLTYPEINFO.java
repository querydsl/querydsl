/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;

public class QSYSTEM_ALLTYPEINFO extends Path.PEntity<java.lang.Object>{
	public final Path.PString colStClsName = _string("COL_ST_CLS_NAME");
	public final Path.PString createParams = _string("CREATE_PARAMS");
	public final Path.PString cstMapClsName = _string("CST_MAP_CLS_NAME");
	public final Path.PString literalPrefix = _string("LITERAL_PREFIX");
	public final Path.PString literalSuffix = _string("LITERAL_SUFFIX");
	public final Path.PString localTypeName = _string("LOCAL_TYPE_NAME");
	public final Path.PString remarks = _string("REMARKS");
	public final Path.PString stdMapClsName = _string("STD_MAP_CLS_NAME");
	public final Path.PString typeName = _string("TYPE_NAME");
	public final Path.PBoolean asProcCol = _boolean("AS_PROC_COL");
	public final Path.PBoolean asTabCol = _boolean("AS_TAB_COL");
	public final Path.PBoolean autoIncrement = _boolean("AUTO_INCREMENT");
	public final Path.PBoolean caseSensitive = _boolean("CASE_SENSITIVE");
	public final Path.PBoolean colStIsSup = _boolean("COL_ST_IS_SUP");
	public final Path.PBoolean cstMapIsSup = _boolean("CST_MAP_IS_SUP");
	public final Path.PBoolean fixedPrecScale = _boolean("FIXED_PREC_SCALE");
	public final Path.PBoolean stdMapIsSup = _boolean("STD_MAP_IS_SUP");
	public final Path.PBoolean unsignedAttribute = _boolean("UNSIGNED_ATTRIBUTE");
	public final Path.PComparable<java.lang.Byte> dataType = _comparable("DATA_TYPE",java.lang.Byte.class);
	public final Path.PComparable<java.lang.Integer> defOrFixedScale = _comparable("DEF_OR_FIXED_SCALE",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Integer> intervalPrecision = _comparable("INTERVAL_PRECISION",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Long> maxPrecAct = _comparable("MAX_PREC_ACT",java.lang.Long.class);
	public final Path.PComparable<java.lang.Integer> maxScaleAct = _comparable("MAX_SCALE_ACT",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Byte> maximumScale = _comparable("MAXIMUM_SCALE",java.lang.Byte.class);
	public final Path.PComparable<java.lang.Long> mcolAct = _comparable("MCOL_ACT",java.lang.Long.class);
	public final Path.PComparable<java.lang.Integer> mcolJdbc = _comparable("MCOL_JDBC",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Integer> minScaleAct = _comparable("MIN_SCALE_ACT",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Byte> minimumScale = _comparable("MINIMUM_SCALE",java.lang.Byte.class);
	public final Path.PComparable<java.lang.Byte> nullable = _comparable("NULLABLE",java.lang.Byte.class);
	public final Path.PComparable<java.lang.Integer> numPrecRadix = _comparable("NUM_PREC_RADIX",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Integer> precision = _comparable("PRECISION",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Byte> searchable = _comparable("SEARCHABLE",java.lang.Byte.class);
	public final Path.PComparable<java.lang.Integer> sqlDataType = _comparable("SQL_DATA_TYPE",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Integer> sqlDatetimeSub = _comparable("SQL_DATETIME_SUB",java.lang.Integer.class);
	public final Path.PComparable<java.lang.Integer> typeSub = _comparable("TYPE_SUB",java.lang.Integer.class);
	
    public QSYSTEM_ALLTYPEINFO(java.lang.String path) {
      	super(java.lang.Object.class, "SYSTEM_ALLTYPEINFO", path);
    }
    public QSYSTEM_ALLTYPEINFO(PathMetadata<?> metadata) {
     	super(java.lang.Object.class, "SYSTEM_ALLTYPEINFO", metadata);
    }
}