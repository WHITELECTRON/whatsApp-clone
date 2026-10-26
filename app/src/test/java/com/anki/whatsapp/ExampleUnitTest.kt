package com.anki.whatsapp

import com.anki.whatsapp.presentation.userregistrationscreen.formatE164PhoneNumber
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun phoneFormat_isCorrect() {
        val result = formatE164PhoneNumber("+91", "63882 65611")
        assertEquals("+916388265611", result)
    }

    @Test
    fun phoneFormatWithDashes_isCorrect() {
        val result = formatE164PhoneNumber("91", "63882-65611")
        assertEquals("+916388265611", result)
    }
}
