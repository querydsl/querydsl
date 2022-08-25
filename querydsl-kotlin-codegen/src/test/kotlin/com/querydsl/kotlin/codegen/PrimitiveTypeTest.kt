package com.querydsl.kotlin.codegen

import com.querydsl.core.util.MathUtils
import org.junit.Assert
import kotlin.test.Test

internal class PrimitiveTypeTest {

    @Test()
    fun castPrimitiveType() {
        Assert.assertThrows("Unsupported target type : double", IllegalArgumentException::class.java) {
            checkCast(1, Double::class.java)
        }
        Assert.assertThrows("Unsupported target type : float", IllegalArgumentException::class.java) {
            checkCast(1, Float::class.java)
        }
        Assert.assertThrows("Unsupported target type : int", IllegalArgumentException::class.java) {
            checkCast(1, Int::class.java)
        }
        Assert.assertThrows("Unsupported target type : long", IllegalArgumentException::class.java) {
            checkCast(1, Long::class.java)
        }
        Assert.assertThrows("Unsupported target type : short", IllegalArgumentException::class.java) {
            checkCast(1, Short::class.java)
        }
        Assert.assertThrows("Unsupported target type : byte", IllegalArgumentException::class.java) {
            checkCast(1, Byte::class.java)
        }
    }

    @Test
    fun castJavaObjectType() {
        checkCast(1, Double::class.javaObjectType)
        checkCast(1, Float::class.javaObjectType)
        checkCast(1, Int::class.javaObjectType)
        checkCast(1, Long::class.javaObjectType)
        checkCast(1, Short::class.javaObjectType)
        checkCast(1, Byte::class.javaObjectType)
    }

    private fun checkCast(value: Number, targetClass: Class<out Number>) {
        val target = MathUtils.cast(value, targetClass)
        Assert.assertSame(targetClass, target.javaClass)
    }

}