package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.color.customGreen
import fr.quentixx.kfilebuilder.ext.isValidTemplateName
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import fr.quentixx.kfilebuilder.json.Node
import fr.quentixx.kfilebuilder.json.TemplateDirectory
import fr.quentixx.kfilebuilder.json.TemplateStorageService
import fr.quentixx.kfilebuilder.tabs.templates.commons.TemplateScreen
import fr.quentixx.kfilebuilder.tabs.templates.commons.TemplateScreenManager
import fr.quentixx.kfilebuilder.treeview.TreeViewBuilder

@Composable
fun CreateTemplateView(
    screenManager: TemplateScreenManager,
    defaultTemplateDirectory: TemplateDirectory? = null,
) {
    val mutableTemplate = remember {
        mutableStateOf(defaultTemplateDirectory ?: TemplateDirectory())
    }
    val mutableNode = remember { mutableStateOf(mutableTemplate.value.content) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CreateTopMenu(screenManager, mutableTemplate)
        Spacer(Modifier.height(16.dp))

        TreeViewBuilder(mutableNode)
    }

    mutableTemplate.value = mutableTemplate.value.copy(content = mutableNode.value)
}

/**
 * Represents the top menu of the template creation screen.
 * @param mutableTemplate the template in a [MutableState].
 */
@Composable
private fun CreateTopMenu(
    screenManager: TemplateScreenManager,
    mutableTemplate: MutableState<TemplateDirectory>
) {
    Spacer(Modifier.height(16.dp))
    Text("Créer une nouvelle template", color = Color.White)

    Row {
        Column {
            Spacer(Modifier.height(16.dp))
            TemplateNameField(mutableTemplate)

            Spacer(Modifier.height(16.dp))
            TemplateDescriptionField(mutableTemplate)
        }

        Spacer(Modifier.width(45.dp))

        // Buttons to process or to go back
        Row(
            Modifier
                .padding(vertical = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GoBackButton(screenManager)
            Spacer(Modifier.width(32.dp))
            SaveTemplateButton(screenManager, mutableTemplate.value)//templateName, templateDescription, currentNode)
        }
    }
}

@Composable
private fun TemplateNameField(
    mutableTemplate: MutableState<TemplateDirectory>
) = TextField(
    value = mutableTemplate.value.name,
    onValueChange = {
        mutableTemplate.value = mutableTemplate.value.copy(name = it)
    },
    label = { Text("Nom de la template") },
    colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White
    )
)

@Composable
private fun TemplateDescriptionField(
    mutableTemplate: MutableState<TemplateDirectory>
) = TextField(
    value = mutableTemplate.value.description,
    onValueChange = { mutableTemplate.value = mutableTemplate.value.copy(description = it) },
    label = { Text("Description de la template (facultatif)") },
    colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White
    )
)

@Composable
private fun GoBackButton(screenManager: TemplateScreenManager) =
    Button(colors = ButtonDefaults.buttonColors(Color.Red, Color.White),
        modifier = Modifier.setOnHoverHandCursorEnabled(),
        onClick = { screenManager.navigateTo(TemplateScreen.MAIN_VIEW) }) {
        Text("Retour")
    }

@Composable
private fun SaveTemplateButton(
    screenManager: TemplateScreenManager,
    template: TemplateDirectory
) = Button(
    modifier = Modifier.setOnHoverHandCursorEnabled(),
    colors = ButtonDefaults.buttonColors(customGreen),
    onClick = {

        if (!isValidTemplate(template)) {
            return@Button
        }

        TemplateStorageService.save(template)
        screenManager.navigateTo(TemplateScreen.MAIN_VIEW)
    }) {
    Text("Enregistrer", color = Color.White)
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
