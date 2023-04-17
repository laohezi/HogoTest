package com.example.nativelib

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val nativeLib = NativeLib()

    @Test
    fun addition_isCorrect() {
        nativeLib.stringFromJNI()
    }
}