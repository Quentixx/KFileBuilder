package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.runtime.Composable
import fr.quentixx.kfilebuilder.components.GenericConfirmWindow
import fr.quentixx.kfilebuilder.json.TemplateStorageService
import fr.quentixx.kfilebuilder.tabs.templates.commons.TemplateScreen
import fr.quentixx.kfilebuilder.tabs.templates.commons.TemplateScreenManager

/**
 * Represents the confirmation view of template deletion.
 * @param screenManager To navigate through the different view.
 */
@Composable
fun DeleteTemplateView(screenManager: TemplateScreenManager) {
    GenericConfirmWindow(
        confirmText = "Voulez-vous vraiment supprimer ce modèle ?",
        onBack = { screenManager.navigateTo(TemplateScreen.MAIN_VIEW) },
        onConfirm = {
            TemplateStorageService.delete(screenManager.selectedTemplate!!.uuid)
        }
    )
}
