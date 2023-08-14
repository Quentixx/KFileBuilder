package fr.quentixx.kfilebuilder.tabs.templates.screens

import androidx.compose.runtime.Composable
import fr.quentixx.kfilebuilder.components.GenericConfirmWindow
import fr.quentixx.kfilebuilder.json.TemplateStorageService
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreen
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreenManager

/**
 * Represents the confirmation view of template deletion.
 * @param screenManager To navigate through the different view.
 */
@Composable
fun DeleteTemplateView(screenManager: TemplateScreenManager) =
    GenericConfirmWindow(
        confirmText = "Voulez-vous vraiment supprimer ce modèle ?",
        onBack = { screenManager.navigateTo(TemplateScreen.LIST_TEMPLATES) },
        onConfirm = {
            TemplateStorageService.delete(screenManager.selectedTemplate!!.uuid)
        }
    )

