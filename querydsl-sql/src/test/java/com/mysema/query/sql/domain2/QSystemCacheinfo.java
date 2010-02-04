package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemCacheinfo is a Querydsl query type for QSystemCacheinfo
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_CACHEINFO")
public class QSystemCacheinfo extends PEntity<QSystemCacheinfo> {

    public final PNumber<Long> cacheBytes = createNumber("CACHE_BYTES", Long.class);

    public final PString cacheFile = createString("CACHE_FILE");

    public final PNumber<Integer> cacheSize = createNumber("CACHE_SIZE", Integer.class);

    public final PNumber<Integer> fileFreeBytes = createNumber("FILE_FREE_BYTES", Integer.class);

    public final PNumber<Integer> fileFreeCount = createNumber("FILE_FREE_COUNT", Integer.class);

    public final PNumber<Long> fileFreePos = createNumber("FILE_FREE_POS", Long.class);

    public final PNumber<Long> maxCacheBytes = createNumber("MAX_CACHE_BYTES", Long.class);

    public final PNumber<Integer> maxCacheCount = createNumber("MAX_CACHE_COUNT", Integer.class);

    public QSystemCacheinfo(String variable) {
        super(QSystemCacheinfo.class, forVariable(variable));
    }

    public QSystemCacheinfo(PEntity<? extends QSystemCacheinfo> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemCacheinfo(PathMetadata<?> metadata) {
        super(QSystemCacheinfo.class, metadata);
    }

}

