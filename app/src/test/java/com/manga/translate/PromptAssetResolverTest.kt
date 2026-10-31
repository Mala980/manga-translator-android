package com.manga.translate

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import androidx.test.core.app.ApplicationProvider
import com.manga.translate.platform.PromptAssetResolver
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PromptAssetResolverTest {
    @Test
    fun `non-traditional locale does not require hant asset`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertFalse(PromptAssetResolver.isTraditionalChinese(context))
    }

    @Test
    fun `brazilian portuguese uses its translation target and prompt variant`() {
        val baseContext = ApplicationProvider.getApplicationContext<Context>()
        val configuration = Configuration(baseContext.resources.configuration).apply {
            setLocales(LocaleList(Locale.forLanguageTag("pt-BR")))
        }
        val context = baseContext.createConfigurationContext(configuration)

        assertEquals("pt_br", PromptAssetResolver.translationTargetKey(context))
        assertEquals(
            "prompts/llm_prompts_pt_br.json",
            PromptAssetResolver.resolve(context, "prompts/llm_prompts.json")
        )
    }

    @Test
    fun `indonesian uses its translation target and prompt variant`() {
        val baseContext = ApplicationProvider.getApplicationContext<Context>()
        val configuration = Configuration(baseContext.resources.configuration).apply {
            setLocales(LocaleList(Locale.forLanguageTag("id-ID")))
        }
        val context = baseContext.createConfigurationContext(configuration)

        assertEquals("id", PromptAssetResolver.translationTargetKey(context))
        assertEquals(
            "prompts/llm_prompts_id.json",
            PromptAssetResolver.resolve(context, "prompts/llm_prompts.json")
        )
        assertEquals(
            "prompts/vl_bubble_prompts_id.json",
            PromptAssetResolver.resolve(context, "prompts/vl_bubble_prompts.json")
        )
    }
}
