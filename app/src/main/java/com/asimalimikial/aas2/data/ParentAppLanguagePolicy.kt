package com.asimalimikial.aas2.data

import android.text.TextUtils
import android.view.View
import java.util.Locale

/** Language policy for parent-facing app chrome; child activity content is independent. */
object ParentAppLanguagePolicy {
    const val SYSTEM = "system"
    const val ENGLISH = "en"
    const val ARABIC = "ar"

    val supportedTags: Set<String> = setOf(SYSTEM, ENGLISH, ARABIC)

    fun normalize(value: String?): String =
        value?.trim()?.lowercase(Locale.ROOT)?.takeIf { it in supportedTags } ?: SYSTEM

    fun localeFor(value: String?, systemLocale: Locale = Locale.getDefault()): Locale =
        when (normalize(value)) {
            ENGLISH -> Locale.ENGLISH
            ARABIC -> Locale.forLanguageTag(ARABIC)
            else -> systemLocale
        }

    fun localeForActiveScreen(
        parentSurfaceActive: Boolean,
        preference: String?,
        systemLocale: Locale = Locale.getDefault()
    ): Locale = if (parentSurfaceActive) localeFor(preference, systemLocale) else systemLocale

    fun isRightToLeft(locale: Locale): Boolean =
        TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_RTL
}
