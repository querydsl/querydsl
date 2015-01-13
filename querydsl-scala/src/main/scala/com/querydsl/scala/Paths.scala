/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.querydsl.scala

import com.querydsl.core.types._
import com.querydsl.core.types.PathMetadataFactory._
import com.querydsl.codegen._

import com.mysema.codegen.model.TypeCategory

import ManifestUtils._


/**
 * Factory for path expressions
 *
 * @author tiwe
 *
 */
object Paths {

  type Metadata[X] = PathMetadata[X]

  def array[T <: Array[_]](t: Class[T], md: Metadata[_]) = new ArrayPath[T](t, md)

  def dsl[T](t: Class[_ <: T], md: Metadata[_]) = new DslPath[T](t, md)

  def simple[T](t: Class[_ <: T], md: Metadata[_]) = new SimplePath[T](t, md)

  def entity[T](t: Class[_ <: T], md: Metadata[_]) = new EntityPathImpl[T](t, md)

  def collection[T, Q <: Ex[_ >: T]](t: Class[T], q: Class[Q], md: Metadata[_]) = {
    new CollectionPath[T,Q](t, q, md)
  }

  def set[T, Q <: Ex[_ >: T]](t: Class[T], q: Class[Q], md: Metadata[_]) = new SetPath[T,Q](t, q, md)

  def list[T, Q <: Ex[_ >: T]](t: Class[T], q: Class[Q], md: Metadata[_]) = new ListPath[T,Q](t, q, md)

  def map[K, V, Q <: Ex[_ >: V]](k: Class[K], v: Class[V], q: Class[Q], md: Metadata[_]) = {
    new MapPath[K, V, Q](k, v, q, md)
  }

  def comparable[T <: Comparable[_]](t: Class[_ <: T], md: Metadata[_]) = new ComparablePath[T](t, md)

  def date[T <: Comparable[_]](t: Class[_ <: T], md: Metadata[_]) = new DatePath[T](t, md)

  def dateTime[T <: Comparable[_]](t: Class[_ <: T], md: Metadata[_]) = new DateTimePath[T](t, md)

  def time[T <: Comparable[_]](t: Class[_ <: T], md: Metadata[_]) = new TimePath[T](t, md)

  def number[T : Numeric](t: Class[T], md: Metadata[_]) = new NumberPath[T](t, md)

  def boolean(md: Metadata[_]) = new BooleanPath(md)

  def string(md: Metadata[_]) = new StringPath(md)

  def enum[T <: Enum[T]](t: Class[T], md: Metadata[_]) = new EnumPath[T](t, md)

  def any[T, Q <: Ex[_ >: T]](parent: Path[_], t: Class[T], q: Class[Q]): Q =  create(t, q, forCollectionAny(parent))

  def create[T, Q <: Ex[_ >: T]](t :Class[T], q: Class[Q], md: Metadata[_]): Q = q match {
    case _ if q == classOf[StringPath] | q == classOf[BooleanPath] => {
      q.getConstructor(classOf[PathMetadata[_]]).newInstance(md)
    }
    case _ => q.getConstructor(classOf[Class[_]], classOf[PathMetadata[_]]).newInstance(t, md)
  }

}

class DslPath[T](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with DslExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable))

}

class SimplePath[T](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with SimpleExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable))

}

class ArrayPath[T <: Array[_]](t: Class[T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with ArrayExpression[T] {

  def this(t: Class[T], variable: String) = this(t, forVariable(variable))

}

class BeanPath[T](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with SimpleExpression[T] {

  import Paths._

  type Mf[X] = Manifest[X]

  def add[P <: Path[_]](path: P): P = path

  def createArray[T <: Array[_]](property: String)(implicit mf: Mf[T]) = add(array(mf, forProperty(property)))

  def createSimple[T](property: String)(implicit mf: Mf[T]) = add(simple(mf, forProperty(property)))

  def createEntity[T](property: String)(implicit mf: Mf[T]) = add(entity(t, forProperty(property)))

  def createCollection[T, Q <: Ex[_ >: T]](property: String)(implicit t: Mf[T], q: Mf[Q]) = add(collection(t, q, forProperty(property)))

  def createSet[T, Q <: Ex[_ >: T]](property: String)(implicit t: Mf[T], q: Mf[Q]) = add(set(t, q, forProperty(property)))

  def createList[T, Q <: Ex[_ >: T]](property: String)(implicit t: Mf[T], q: Mf[Q]) = add(list(t, q, forProperty(property)))

  def createMap[K, V, Q <: Ex[_ >: V]](property: String)(implicit k: Mf[K], v: Mf[V], q: Mf[Q]) = add(map(k, v, q, forProperty(property)))

  def createComparable[T <: Comparable[_]](property: String)(implicit mf: Mf[T]) = add(comparable(mf, forProperty(property)))

  def createDate[T <: Comparable[_]](property: String)(implicit mf: Mf[T]) = add(date(mf, forProperty(property)))

  def createDateTime[T <: Comparable[_]](property: String)(implicit mf: Mf[T]) = add(dateTime(mf, forProperty(property)))

  def createTime[T <: Comparable[_]](property: String)(implicit mf: Mf[T]) = add(time(mf, forProperty(property)))

  def createNumber[T](property: String)(implicit num: Numeric[T], mf: Mf[T]) = add(number[T](mf, forProperty(property)))

  def createBoolean(property: String) = add(boolean(forProperty(property)))

  def createString(property: String) = add(string(forProperty(property)))

  def createEnum[T <: Enum[T]](property: String)(implicit mf: Mf[T]) = add(enum(mf, forProperty(property)))

  private def forProperty(property: String) = PathMetadataFactory.forProperty(this, property)

}

class EntityPathImpl[T](t: Class[_ <: T], md: PathMetadata[_])
  extends BeanPath[T](t, md) with EntityPath[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable))

  def getMetadata(path: Path[_]): AnyRef = null

}

class CollectionPath[T, Q <: Ex[_ >: T]](t: Class[T], q: Class[Q], md: PathMetadata[_])
  extends PathImpl[java.util.Collection[T]](classOf[java.util.Collection[T]], md) with CollectionExpression[T,Q] {

  def this(t: Class[T],  q: Class[Q], variable: String) = this(t, q, forVariable(variable))

  def getParameter(i: Int) = t

  lazy val any: Q = Paths.any(this, t, q)

}

class SetPath[T, Q <: Ex[_ >: T]](t: Class[T],  q: Class[Q], md: PathMetadata[_])
  extends PathImpl[java.util.Set[T]](classOf[java.util.Set[T]], md) with SetExpression[T,Q] {

  def this(t: Class[T], q: Class[Q], variable: String) = this(t, q, forVariable(variable))

  def getParameter(i: Int) = t

  lazy val any: Q = Paths.any(this, t, q)

}

class ListPath[T, Q <: Ex[_ >: T]](t: Class[T],  q: Class[Q], md: PathMetadata[_])
  extends PathImpl[java.util.List[T]](classOf[java.util.List[T]], md) with ListExpression[T,Q] {

  def this(t: Class[T], q: Class[Q], variable: String) = this(t, q, forVariable(variable))

  def getParameter(i: Int) = t

  lazy val any: Q = Paths.any(this, t, q)

  def get(i: Int): Q = Paths.create(t, q, forListAccess(this, i))

  def get(i: Ex[Integer]): Q = Paths.create(t, q, forListAccess(this, i))

}

class MapPath[K, V, Q <: Ex[_ >: V]](k: Class[K], v: Class[V], q: Class[Q], md: PathMetadata[_])
  extends PathImpl[java.util.Map[K, V]](classOf[java.util.Map[K, V]], md) with MapExpression[K, V, Q] {

  def this(k: Class[K], v: Class[V], q: Class[Q], variable: String) = this(k, v, q, forVariable(variable))

  def getParameter(i: Int): Class[_] = { if (i == 0) k else v }

  def get(key: K) = Paths.create(v, q, forMapAccess(this, key))

  def get(key: Ex[K]) = Paths.create(v, q, forMapAccess(this, key))

}

class ComparablePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with ComparableExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable))

}

class NumberPath[T : Numeric](t: Class[T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with NumberExpression[T] {

  def this(t: Class[T], variable: String) = this(t, forVariable(variable))
  override def numeric = implicitly[Numeric[T]]

}

class BooleanPath(md: PathMetadata[_])
  extends PathImpl[java.lang.Boolean](classOf[java.lang.Boolean], md) with BooleanExpression {

  def this(variable: String) = this(forVariable(variable))

}

class StringPath(md: PathMetadata[_])
  extends PathImpl[String](classOf[String], md) with StringExpression {

  def this(variable: String) = this(forVariable(variable))

}

class DatePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with DateExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable))

}

class DateTimePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with DateTimeExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable))

}

class TimePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with TimeExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable))

}

class EnumPath[T <: Enum[T]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with EnumExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable))

}
