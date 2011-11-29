package com.mysema.scala

/**
 * @author tiwe
 *
 */
object ManifestUtils {
  
  implicit def toClass[X](mf: Manifest[X]) = mf.erasure.asInstanceOf[Class[X]]
  
}