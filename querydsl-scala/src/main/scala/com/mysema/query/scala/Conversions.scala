package com.mysema.query.scala;

import com.mysema.query.alias.Alias._
import com.mysema.query.types.path._

/**
 * @author tiwe
 *
 */
object Conversions {
    implicit def _boolean(b: Boolean): PBoolean = $(b);
    implicit def _string(s: String): PString = $(s);
    implicit def _comparable(c: Comparable[_]): PComparable[_] = $(c);
    implicit def _date(d: java.sql.Date): PDate[java.sql.Date] = $(d);
    implicit def _dateTime(d: java.util.Date): PDateTime[java.util.Date] = $(d);
    implicit def _time(t: java.sql.Time): PTime[java.sql.Time] = $(t);
    //implicit def num[N <: Number>](n: N): PNumber[N] = $(n);
}
