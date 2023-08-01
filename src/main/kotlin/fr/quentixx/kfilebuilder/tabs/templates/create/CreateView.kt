package fr.quentixx.kfilebuilder.tabs.templates.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import fr.quentixx.kfilebuilder.json.TemplateDirectory
import fr.quentixx.kfilebuilder.json.TemplatesService
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreen
import fr.quentixx.kfilebuilder.tabs.templates.TemplateScreenManager

@Composable
fun CreateTemplateView(screenManager: TemplateScreenManager) {
    val templateName = remember { mutableStateOf("") }
    val templateDescription = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CreateTopMenu(screenManager, templateName, templateDescription)
        Spacer(Modifier.height(16.dp))

        TemplateBuilderView() // Show the Tree View
    }
}

/**
 * Represents the top menu of the template creation screen.
 * @param templateName the name of the template in a [MutableState].
 * @param templateDescription the description of the template in a [MutableState].
 */
@Composable
private fun CreateTopMenu(
    screenManager: TemplateScreenManager,
    templateName: MutableState<String>,
    templateDescription: MutableState<String>
) {
    Spacer(Modifier.height(16.dp))
    Text("Créer une nouvelle template", color = Color.White)

    Row {
        Column {
            Spacer(Modifier.height(16.dp))
            TemplateNameField(screenManager, templateName)

            Spacer(Modifier.height(16.dp))
            TemplateDescriptionField(templateDescription)
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
            SaveTemplateButton(screenManager, templateName, templateDescription)
        }
    }
}

@Composable
private fun TemplateNameField(
    screenManager: TemplateScreenManager,
    templateName: MutableState<String>
) = TextField(
    value = templateName.value,
    onValueChange = {
        templateName.value = it
    },
    label = { Text("Nom de la template") },
    colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White
    )
)

@Composable
private fun TemplateDescriptionField(templateDescription: MutableState<String>) = TextField(
    value = templateDescription.value,
    onValueChange = { newValue: String -> templateDescription.value = newValue },
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
    templateName: MutableState<String>,
    templateDescription: MutableState<String>,
) = Button(modifier = Modifier.setOnHoverHandCursorEnabled(), onClick = {

    templateName.value.apply {
        if (isEmpty() || TemplatesService.isExists(this)) {
            return@Button
        }
    }

    TemplatesService.save(
        TemplateDirectory(templateName.value, templateDescription.value, emptyList())
    )
    screenManager.navigateTo(TemplateScreen.MAIN_VIEW)
}) {
    Text("Enregistrer")
}