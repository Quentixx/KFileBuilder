package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.runtime.*
import fr.quentixx.kfilebuilder.json.TemplateStorageService
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreen.*
import fr.quentixx.kfilebuilder.tabs.templates.screens.DeleteTemplateView
import fr.quentixx.kfilebuilder.tabs.templates.screens.GenerateTemplateView
import fr.quentixx.kfilebuilder.tabs.templates.screens.ManageTemplateView
import fr.quentixx.kfilebuilder.tabs.templates.screens.TemplatesListView
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

/**
 * Represents the Templates tab.
 * This tab can show different screens depending on the context. See [TemplateScreen].
 */
@Composable
fun TemplatesTab() {
    val templates = TemplateStorageService.getAll()
    val screenManager = remember { TemplateScreenManager() }
    val screen = screenManager.currentView.value

    when (screen) {
        LIST_TEMPLATES -> TemplatesListView(screenManager, templates)
        MANAGE_TEMPLATE -> ManageTemplateView(screenManager)
        DELETE_TEMPLATE_CONFIRMATION -> DeleteTemplateView(screenManager)
        GENERATE_TEMPLATE -> GenerateTemplateView(screenManager)
    }

    logger.info { "The current screen is now $screen" }
}
