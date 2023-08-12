package fr.quentixx.kfilebuilder.tabs.templates.commons

import androidx.compose.runtime.*
import fr.quentixx.kfilebuilder.json.TemplateStorageService
import fr.quentixx.kfilebuilder.tabs.templates.GenerateTemplateView
import fr.quentixx.kfilebuilder.tabs.templates.CreateTemplateView
import fr.quentixx.kfilebuilder.tabs.templates.DeleteTemplateView
import fr.quentixx.kfilebuilder.tabs.templates.MainView
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

@Composable
fun TemplatesTab() {
    val templates = TemplateStorageService.getAll()
    val screenManager = remember { TemplateScreenManager() }

    when (screenManager.currentView.value) {
        TemplateScreen.MAIN_VIEW -> MainView(templates, screenManager)
        TemplateScreen.CREATE_TEMPLATE_VIEW -> CreateTemplateView(screenManager)
        TemplateScreen.EDIT_TEMPLATE_VIEW -> EditTemplateView(screenManager)
        TemplateScreen.DELETE_TEMPLATE_CONFIRMATION -> DeleteTemplateView(screenManager)
        TemplateScreen.GENERATE_TEMPLATE_VIEW -> GenerateTemplateView(screenManager)
    }
}


@Composable
fun EditTemplateView(screenManager: TemplateScreenManager) {
    CreateTemplateView(screenManager, screenManager.selectedTemplate)
}
