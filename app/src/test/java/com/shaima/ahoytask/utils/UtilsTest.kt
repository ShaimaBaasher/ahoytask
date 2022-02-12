package com.shaima.ahoytask.utils

import com.shaima.api.repository.Utils
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UtilsTest {

    @Test
    fun inputIsNotValid() {
        val result = Utils.checkValid("")
        assertEquals(false, result)
    }

    @Test
    fun inputIsLessThan4Chars() {
        val result = Utils.checkValid("shu")
        assertEquals(false, result)
    }

    @Test
    fun inputIsValid() {
        val result = Utils.checkValid("sudan")
        assertEquals(true, result)
    }

}