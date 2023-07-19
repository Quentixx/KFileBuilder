package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.runtime.mutableStateOf

/**
 * Enum class representing different screens in Templates tab.
 */
enum class TemplateScreen {
    MAIN_VIEW,
    CREATE_TEMPLATE_VIEW,
    EDIT_TEMPLATE_VIEW
}

/**
 * Class responsible for managing the current template screen.
 */
class TemplateScreenManager {
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