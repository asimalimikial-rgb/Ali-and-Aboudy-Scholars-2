package com.asimalimikial.aas2.data

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ParentAppLanguagePolicyTest {
    @Test
    fun unsupportedOrMissingPreferenceFollowsSystemLanguage() {
        assertEquals(ParentAppLanguagePolicy.SYSTEM, ParentAppLanguagePolicy.normalize(null))
        assertEquals(ParentAppLanguagePolicy.SYSTEM, ParentAppLanguagePolicy.normalize("fr"))
        assertEquals(ParentAppLanguagePolicy.SYSTEM, ParentAppLanguagePolicy.normalize(""))
    }

    @Test
    fun supportedLanguageTagsAreNormalized() {
        assertEquals(ParentAppLanguagePolicy.ARABIC, ParentAppLanguagePolicy.normalize(" AR "))
        assertEquals(ParentAppLanguagePolicy.ENGLISH, ParentAppLanguagePolicy.normalize("EN"))
    }

    @Test
    fun parentLanguageOverrideDoesNotChangeLearnerFacingScreens() {
        assertEquals(
            "fr",
            ParentAppLanguagePolicy.localeForActiveScreen(false, "ar", Locale.FRENCH).language
        )
        assertEquals(
            "ar",
            ParentAppLanguagePolicy.localeForActiveScreen(true, "ar", Locale.FRENCH).language
        )
    }

    @Test
    fun localeSelectionAndDirectionAreConsistent() {
        assertEquals("ar", ParentAppLanguagePolicy.localeFor("ar").language)
        assertEquals("en", ParentAppLanguagePolicy.localeFor("en").language)
        assertEquals("fr", ParentAppLanguagePolicy.localeFor("system", Locale.FRENCH).language)
        assertTrue(ParentAppLanguagePolicy.isRightToLeft(Locale.forLanguageTag("ar")))
        assertFalse(ParentAppLanguagePolicy.isRightToLeft(Locale.ENGLISH))
    }
}
