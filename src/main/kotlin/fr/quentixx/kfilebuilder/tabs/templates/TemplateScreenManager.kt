package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.runtime.mutableStateOf
import fr.quentixx.kfilebuilder.json.TemplateDirectory
import fr.quentixx.kfilebuilder.tabs.templates.screens.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

/**
 * Enumeration of the different screens can be opened in Templates tab.
 * @see [TemplateScreenManager] to navigate through the TemplatesTab.
 */
enum class TemplateScreen {
    /**
     * Shows the list of stored templates.
     * @see [TemplatesListView] to access the controller of this screen.
     */
    LIST_TEMPLATES,

    /**
     * Manage an existing or new template.
     * @see [ManageTemplateView] to access the controller of this screen.
     */
    MANAGE_TEMPLATE,

    /**
     * Generate the structure of the template in the computer.
     * @see [GenerateTemplateView] to access the controller of this screen.
     */
    GENERATE_TEMPLATE,

    /**
     * Confirmation of the deletion of a template.
     * @see [DeleteTemplateView] to access the controller of this screen.
     */
    DELETE_TEMPLATE_CONFIRMATION
}


/**
 * Class responsible for managing the current template screen.
 */
class TemplateScreenManager {
    var selectedTemplate: TemplateDirectory? = null
        private set(value) {
            field = value

            logger.info {
                if (value == null) {
                    "No template selected now."
                } else {
                    "The selected template is now ${value.name}."
                }
            }
        }

    // The current template screen state.
    val currentView = mutableStateOf(TemplateScreen.LIST_TEMPLATES)

    /**
     * Navigates to the specified template screen.
     *
     * @param screen The target template screen to navigate to.
     * @param providedTemplate The template provided to use in the screen.
     */
    fun navigateTo(screen: TemplateScreen, providedTemplate: TemplateDirectory? = null) {
        if (selectedTemplate != providedTemplate)
            selectedTemplate = providedTemplate

        currentView.value = screen
    }
}
