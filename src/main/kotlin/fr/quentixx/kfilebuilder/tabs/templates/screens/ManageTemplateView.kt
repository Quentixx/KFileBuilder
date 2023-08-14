package fr.quentixx.kfilebuilder.tabs.templates.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.color.customGreen
import fr.quentixx.kfilebuilder.components.GenericButton
import fr.quentixx.kfilebuilder.ext.isValidTemplateName
import fr.quentixx.kfilebuilder.json.Node
import fr.quentixx.kfilebuilder.json.TemplateDirectory
import fr.quentixx.kfilebuilder.json.TemplateStorageService
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreen
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreenManager
import fr.quentixx.kfilebuilder.treeview.TreeViewBuilder

/**
 * Represents the view that allow user to manage an existing or new [TemplateDirectory].
 * This view is structured as follows: Top menu (to define the name and description of the template, back and save buttons)
 *                                     and TreeViewBuilder (to see and edit the tree structure of the template)
 * @param screenManager The manager to navigate through the Templates tab.
 * @param existingTemplate The existing template to edit.
 *                         If no template are provided, the manager will create a new template in the storage.
 */
@Composable
fun ManageTemplateView(
    screenManager: TemplateScreenManager,
    existingTemplate: TemplateDirectory? = screenManager.selectedTemplate,
) {
    val mutableTemplate = remember {
        mutableStateOf(existingTemplate ?: TemplateDirectory())
    }
    val mutableNode = remember { mutableStateOf(mutableTemplate.value.content) }
    val isCreateMode = existingTemplate == null

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TemplateTopMenu(screenManager, mutableTemplate, isCreateMode)
        Spacer(Modifier.height(16.dp))
        TreeViewBuilder(mutableNode)
    }

    mutableTemplate.value = mutableTemplate.value.copy(content = mutableNode.value)
}

@Composable
private fun TemplateTopMenu(
    screenManager: TemplateScreenManager,
    mutableTemplate: MutableState<TemplateDirectory>,
    createMode: Boolean,
) {
    Spacer(Modifier.height(16.dp))
    Text(
        if (createMode) {
            "Créer un nouveau modèle"
        } else {
            "Editer un modèle existant"
        }, color = Color.White
    )

    Row {
        Column {
            Spacer(Modifier.height(16.dp))
            TemplateNameField(mutableTemplate)
            Spacer(Modifier.height(16.dp))
            TemplateDescriptionField(mutableTemplate)
        }

        Spacer(Modifier.width(45.dp))

        // Buttons to process or to go back
        Row(Modifier.padding(vertical = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GoBackButton(screenManager)
            Spacer(Modifier.width(32.dp))
            SaveTemplateButton(screenManager, mutableTemplate.value)
        }
    }
}

@Composable
private fun TemplateNameField(
    mutableTemplate: MutableState<TemplateDirectory>
) = TextField(mutableTemplate.value.name,
    onValueChange = {
        mutableTemplate.value = mutableTemplate.value.copy(name = it)
    },
    label = { Text("Nom du modèle") },
    colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White
    )
)

@Composable
private fun TemplateDescriptionField(
    mutableTemplate: MutableState<TemplateDirectory>
) = TextField(mutableTemplate.value.description,
    onValueChange = { mutableTemplate.value = mutableTemplate.value.copy(description = it) },
    label = { Text("Description du modèle (facultatif)") },
    colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White
    )
)

@Composable
private fun GoBackButton(screenManager: TemplateScreenManager) = GenericButton("Retour", Color.Red) {
    screenManager.navigateTo(TemplateScreen.LIST_TEMPLATES)
}

@Composable
private fun SaveTemplateButton(
    screenManager: TemplateScreenManager,
    template: TemplateDirectory
) = GenericButton("Enregistrer", customGreen) {
    if (!isValidTemplate(template)) {
        return@GenericButton
    }

    TemplateStorageService.save(template)
    screenManager.navigateTo(TemplateScreen.LIST_TEMPLATES)
}

/**
 * Know if a template is ready for use in storage.
 * A template is "valid" when its name is not empty
 * and contains no more than 16 characters,
 * it must not contain duplicates of the file names.
 *
 * @param template The template to check for validity.
 *
 * @return true if the template is ready for use in storage, false otherwise.
 */
private fun isValidTemplate(template: TemplateDirectory): Boolean {
    val uuidExists = TemplateStorageService.isExistsByUuid(template.uuid)
    val nameExists = TemplateStorageService.isExistsByName(template.name)

    return !(!template.name.isValidTemplateName()
            || isNodeContainsDuplicates(template.content)
            || (!uuidExists && nameExists))
}

private fun isNodeContainsDuplicates(node: Node): Boolean {
    val childPaths = mutableSetOf<String>()
    node.children.forEach { childNode ->
        if (childPaths.contains(childNode.path))
            return true
        childPaths.add(childNode.path)

        if (isNodeContainsDuplicates(childNode))
            return true
    }
    return false
}
