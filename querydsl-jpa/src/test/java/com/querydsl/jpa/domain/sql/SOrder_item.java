package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SOrder_item is a Querydsl querydsl type for SOrder_item
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SOrder_item extends com.querydsl.sql.RelationalPathBase<SOrder_item> {

    private static final long serialVersionUID = -2131249686;

    public static final SOrder_item order_item_ = new SOrder_item("order__item_");

    public final NumberPath<Integer> _index = createNumber("_index", Integer.class);

    public final NumberPath<Long> itemsId = createNumber("itemsId", Long.class);

    public final NumberPath<Long> order_id = createNumber("order_id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SOrder_item> primary = createPrimaryKey(_index, order_id);

    public final com.querydsl.sql.ForeignKey<SItem> fk1b5e8cbe7640c8cf = createForeignKey(itemsId, "id");

    public final com.querydsl.sql.ForeignKey<SOrder> fk1b5e8cbeb968f515 = createForeignKey(order_id, "id");

    public SOrder_item(String variable) {
        super(SOrder_item.class, forVariable(variable), "", "order__item_");
        addMetadata();
    }

    public SOrder_item(String variable, String schema, String table) {
        super(SOrder_item.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SOrder_item(Path<? extends SOrder_item> path) {
        super(path.getType(), path.getMetadata(), "", "order__item_");
        addMetadata();
    }

    public SOrder_item(PathMetadata<?> metadata) {
        super(SOrder_item.class, metadata, "", "order__item_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(_index, ColumnMetadata.named("_index").withIndex(3).ofType(4).withSize(10).notNull());
        addMetadata(itemsId, ColumnMetadata.named("items_id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(order_id, ColumnMetadata.named("order__id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

