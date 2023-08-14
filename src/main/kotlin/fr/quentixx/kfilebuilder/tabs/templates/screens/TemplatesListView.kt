package fr.quentixx.kfilebuilder.tabs.templates.screens

import fr.quentixx.kfilebuilder.components.Scrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.color.customGreen
import fr.quentixx.kfilebuilder.components.GenericButton
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import fr.quentixx.kfilebuilder.json.TemplateDirectory
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreen
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreenManager

/**
 * All templates are listed here, with the name of the template and control buttons.
 * @param screenManager The manager to navigate through the Templates tab.
 * @param templates The list of templates to show in this view.
 */
@Composable
fun TemplatesListView(
    screenManager: TemplateScreenManager,
    templates: List<TemplateDirectory>
) {
    val listState = rememberLazyListState()
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))
            TopMenu(screenManager)
            Spacer(modifier = Modifier.height(16.dp))
            Box(Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(templates) { template ->
                        TemplateLine(screenManager, template)
                        Spacer(Modifier.height(16.dp))
                    }
                }
                // Adding Vertical Scrollbar
                Scrollbar(listState)
            }
        }
    }
}

@Composable
private fun TopMenu(screenManager: TemplateScreenManager) = Row(
    Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
) {
    GenericButton("Créer un modèle") {
        screenManager.navigateTo(TemplateScreen.MANAGE_TEMPLATE)
    }
}


@Composable
private fun TemplateLine(
    screenManager: TemplateScreenManager,
    templateDirectory: TemplateDirectory
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            Modifier.requiredWidth(450.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(templateDirectory.name,
                color = Color.LightGray,
                textAlign = TextAlign.End
            )
            Spacer(Modifier.width(32.dp))
            EditTemplateButton(screenManager, templateDirectory)
            Spacer(Modifier.width(16.dp))
            GenerateTemplateButton(screenManager, templateDirectory)
            Spacer(Modifier.width(4.dp).height(1.dp))
            DeleteTemplateButton(screenManager, templateDirectory)
        }
    }
}

@Composable
private fun EditTemplateButton(
    screenManager: TemplateScreenManager,
    templateDirectory: TemplateDirectory
) = GenericButton("Editer", Color.Gray) {
    screenManager.navigateTo(TemplateScreen.MANAGE_TEMPLATE, templateDirectory)
}

@Composable
private fun GenerateTemplateButton(
    screenManager: TemplateScreenManager,
    templateDirectory: TemplateDirectory
) = GenericButton("Générer", customGreen) {
    screenManager.navigateTo(TemplateScreen.GENERATE_TEMPLATE, templateDirectory)
}

@Composable
private fun DeleteTemplateButton(
    screenManager: TemplateScreenManager,
    templateDirectory: TemplateDirectory
) = IconButton(
    onClick = { screenManager.navigateTo(TemplateScreen.DELETE_TEMPLATE_CONFIRMATION, templateDirectory) },
    modifier = Modifier
        .size(45.dp)
        .setOnHoverHandCursorEnabled()
) {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = "Supprimer",
        tint = Color.Red
    )
}
