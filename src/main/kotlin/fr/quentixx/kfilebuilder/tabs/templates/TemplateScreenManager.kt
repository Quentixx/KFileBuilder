package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.runtime.mutableStateOf
import fr.quentixx.kfilebuilder.json.TemplateDirectory

/**
 * Enum class representing different screens in Templates tab.
 */
enum class TemplateScreen {
    MAIN_VIEW,
    CREATE_TEMPLATE_VIEW,
    EDIT_TEMPLATE_VIEW,
    BUILD_TEMPLATE_VIEW,
    DELETE_TEMPLATE_CONFIRMATION
}

/**
 * Class responsible for managing the current template screen.
 */
class TemplateScreenManager {
    var selectedTemplate: TemplateDirectory? = null

    // The current template screen state.
    val currentView = mutableStateOf(TemplateScreen.MAIN_VIEW)

    /**
     * Navigates to the specified template screen.
     *
     * @param screen The target template screen to navigate to.
     */
    fun navigateTo(screen: TemplateScreen) {
        currentView.value = screen
    }
}
