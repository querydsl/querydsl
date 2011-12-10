/*
 * Copyright 2011, Mysema Ltd
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

package com.mysema.query.scala.escaped;

import com.mysema.query.types._;
import com.mysema.query.types.PathMetadataFactory._;

import com.mysema.query.alias._;

/**
 * @author tiwe
 *
 */
object Paths {

  def array[T <: Array[_]](t: Class[T], md: PathMetadata[_]) = new ArrayPath[T](t, md);

  def simple[T](t: Class[_ <: T], md: PathMetadata[_]) = new SimplePath[T](t, md);

  def entity[T](t: Class[_ <: T], md: PathMetadata[_]) = new EntityPathImpl[T](t, md);

  def collection[T](t: Class[_ <: T], md: PathMetadata[_]) = new CollectionPath[T](t, md);

  def set[T](t: Class[_ <: T], md: PathMetadata[_]) = new SetPath[T](t, md);

  def list[T](t: Class[_ <: T], md: PathMetadata[_]) = new ListPath[T](t, md);

  def map[K, V](k: Class[_ <: K], v: Class[_ <: V], md: PathMetadata[_]) = new MapPath[K, V](k, v, md);

  def comparable[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_]) = new ComparablePath[T](t, md);

  def date[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_]) = new DatePath[T](t, md);

  def dateTime[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_]) = new DateTimePath[T](t, md);

  def time[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_]) = new TimePath[T](t, md);

  def number[T <: Number with Comparable[T]](t: Class[_ <: T], md: PathMetadata[_]) = new NumberPath[T](t, md);

  def boolean(md: PathMetadata[_]) = new BooleanPath(md);

  def string(md: PathMetadata[_]) = new StringPath(md);

  def enum[T <: Enum[T]](t: Class[T], md: PathMetadata[_]) = new EnumPath[T](t, md);
}

class PathFactoryImpl extends PathFactory {

  def createArrayPath[T](t: Class[Array[T with Object]], md: PathMetadata[_]) = Paths.array(t, md);

  def createEntityPath[T](t: Class[T], md: PathMetadata[_]) = Paths.entity(t, md);

  def createSimplePath[T](t: Class[T], md: PathMetadata[_]) = Paths.simple(t, md);

  def createComparablePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.comparable(t, md);

  def createEnumPath[T <: Enum[T]](t: Class[T], md: PathMetadata[_]) = Paths.enum(t, md);

  def createDatePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.date(t, md);

  def createTimePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.time(t, md);

  def createDateTimePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.dateTime(t, md);

  def createNumberPath[T <: Number with Comparable[T]](t: Class[T], md: PathMetadata[_]) = Paths.number(t, md);

  def createBooleanPath(md: PathMetadata[_]) = Paths.boolean(md);

  def createStringPath(md: PathMetadata[_]) = Paths.string(md);

  def createListPath[T](t: Class[T], md: PathMetadata[_]) = Paths.list(t, md);

  def createSetPath[T](t: Class[T], md: PathMetadata[_]) = Paths.set(t, md);

  def createCollectionPath[T](t: Class[T], md: PathMetadata[_]) = Paths.collection(t, md);

  def createMapPath[K, V](k: Class[K], v: Class[V], md: PathMetadata[_]) = Paths.map(k, v, md);

}

class SimplePath[T](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with SimpleExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this

}

class ArrayPath[T <: Array[_]](t: Class[T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with ArrayExpression[T] {

  def this(t: Class[T], variable: String) = this(t, forVariable(variable));

  def ~ = this

}

class EntityPathImpl[T](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with SimpleExpression[T] with EntityPath[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this
}

class CollectionPath[T](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[java.util.Collection[T]](classOf[java.util.Collection[T]], md) with CollectionExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this

  def getParameter(i: Int) = t;

}

class SetPath[T](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[java.util.Set[T]](classOf[java.util.Set[T]], md) with SetExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this

  def getParameter(i: Int) = t;

}

class ListPath[T](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[java.util.List[T]](classOf[java.util.List[T]], md) with ListExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this

  def getParameter(i: Int) = t;

}

class MapPath[K, V](k: Class[_ <: K], v: Class[_ <: V], md: PathMetadata[_])
  extends PathImpl[java.util.Map[K, V]](classOf[java.util.Map[K, V]], md) with MapExpression[K, V] {

  def this(k: Class[_ <: K], v: Class[_ <: V], variable: String) = this(k, v, forVariable(variable));

  def ~ = this

  def getParameter(i: Int): Class[_] = { if (i == 0) k else v }

}

class ComparablePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with ComparableExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this
}

class NumberPath[T <: Number with Comparable[T]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with NumberExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this
}

class BooleanPath(md: PathMetadata[_])
  extends PathImpl[java.lang.Boolean](classOf[java.lang.Boolean], md) with BooleanExpression {

  def this(variable: String) = this(forVariable(variable));

  def ~ = this

}

class StringPath(md: PathMetadata[_])
  extends PathImpl[String](classOf[String], md) with StringExpression {

  def this(variable: String) = this(forVariable(variable));

  def ~ = this

}

class DatePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with DateExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this

}

class DateTimePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with DateTimeExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this

}

class TimePath[T <: Comparable[_]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with TimeExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this

}

class EnumPath[T <: Enum[T]](t: Class[_ <: T], md: PathMetadata[_])
  extends PathImpl[T](t, md) with EnumExpression[T] {

  def this(t: Class[_ <: T], variable: String) = this(t, forVariable(variable));

  def ~ = this

}
