package com.mysema.query.scala.sql;
import com.mysema.query.scala.BeanPath
import com.mysema.query.sql._
import com.mysema.query.types._
import com.mysema.query.types.PathMetadataFactory._
import java.util.ArrayList;
import scala.reflect.BeanProperty

/**
 * @author tiwe
 *
 */
class RelationalPathImpl[T](t: Class[_ <: T], md: PathMetadata[_])
  extends BeanPath[T](t, md) with RelationalPath[T] {
    
  private var primaryKey: PrimaryKey[T] = _;
  
  @BeanProperty
  val columns: java.util.List[Path[_]] = new ArrayList[Path[_]];

  @BeanProperty
  val foreignKeys: java.util.List[ForeignKey[_]] = new ArrayList[ForeignKey[_]];
  
  @BeanProperty
  val inverseForeignKeys: java.util.List[ForeignKey[_]] = new ArrayList[ForeignKey[_]];
  
  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));
  
  override def add[P <: Path[_]](p: P): P = { columns.add(p); p; }
  
  // FIXME
  //def all: Array[Path[_]] = columns.toArray(new Array[Path[_]](columns.size));
  
  // createPrimaryKey
  
  // createForeignKey
  
  def getPrimaryKey = primaryKey;
  
  def getSchemaName = getType.getAnnotation(classOf[Schema]).value;
  
  def getTableName = getType.getAnnotation(classOf[Table]).value;
  
}