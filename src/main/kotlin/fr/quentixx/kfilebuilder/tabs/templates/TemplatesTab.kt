package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.runtime.*
import fr.quentixx.kfilebuilder.json.TemplatesService
import mu.KotlinLogging
import fr.quentixx.kfilebuilder.tabs.templates.create.CreateTemplateView

private val logger = KotlinLogging.logger { }

@Composable
fun TemplatesTab() {
    val templates = TemplatesService.getAll()
    val screenManager = remember { TemplateScreenManager() }

    when (screenManager.currentView.value) {
        TemplateScreen.MAIN_VIEW -> MainView(templates, screenManager)
        TemplateScreen.CREATE_TEMPLATE_VIEW -> CreateTemplateView(screenManager)
        TemplateScreen.EDIT_TEMPLATE_VIEW -> EditTemplateView(screenManager)
    }
}


@Composable
fun EditTemplateView(screenManager: TemplateScreenManager) {
    CreateTemplateView(screenManager, screenManager.selectedTemplate)
}
