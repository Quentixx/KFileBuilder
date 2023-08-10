package fr.quentixx.kfilebuilder.tabs.templates

import fr.quentixx.kfilebuilder.components.Scrollbar
import androidx.compose.foundation.background
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
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import fr.quentixx.kfilebuilder.json.Node
import fr.quentixx.kfilebuilder.json.TemplateDirectory
import java.awt.Desktop
import java.io.File
import java.io.IOException

/**
 * The main view of templates. All templates are listed here, with the name of the template and buttons to edit or delete it.
 */
@Composable
fun MainView(
    templates: List<TemplateDirectory>,
    screenManager: TemplateScreenManager
) {
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))

            TopMenu(screenManager)

            Spacer(modifier = Modifier.height(16.dp))

            val listState = rememberLazyListState()

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(templates) { template ->
                        TemplateLine(
                            screenManager,
                            template
                        ) // Show the template line with [name, edit button, delete button]
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // Adding Vertical Scrollbar
                Scrollbar(listState)
            }
        }

        Box(Modifier.fillMaxSize().background(Color.Gray)) {
            // template details
        }
    }
}

@Composable
fun TopMenu(screenManager: TemplateScreenManager) = Row(
    Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
) {
    Button(
        onClick = {
            screenManager.navigateTo(TemplateScreen.CREATE_TEMPLATE_VIEW)
        },
        modifier = Modifier.setOnHoverHandCursorEnabled()
    ) {
        Text("Créer une template", color = Color.White)
    }
}


@Composable
fun TemplateLine(
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
            Text(
                text = templateDirectory.name,
                color = Color.LightGray,
                textAlign = TextAlign.End
            )
            Spacer(Modifier.width(32.dp))
            EditTemplateIcon(screenManager, templateDirectory)
            Spacer(Modifier.width(16.dp))
            BuildTemplateIcon(screenManager, templateDirectory)
            Spacer(Modifier.width(4.dp).height(1.dp))
            DeleteTemplateIcon(screenManager, templateDirectory)
        }
    }
}

@Composable
fun EditTemplateIcon(
    screenManager: TemplateScreenManager,
    templateDirectory: TemplateDirectory
) = Button(
    onClick = {
        screenManager.selectedTemplate = templateDirectory
        screenManager.navigateTo(TemplateScreen.EDIT_TEMPLATE_VIEW)
    },
    modifier = Modifier
        .size(100.dp, 35.dp)
        .setOnHoverHandCursorEnabled()
) {
    Text("Editer", color = Color.White)
}

@Composable
fun DeleteTemplateIcon(
    screenManager: TemplateScreenManager,
    templateDirectory: TemplateDirectory
) =
    IconButton(
        onClick = {
            screenManager.selectedTemplate = templateDirectory
            screenManager.navigateTo(TemplateScreen.DELETE_TEMPLATE_CONFIRMATION)
        },
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

@Composable
fun BuildTemplateIcon(
    screenManager: TemplateScreenManager,
    templateDirectory: TemplateDirectory
) = Button(
    onClick = {
        val file = buildNodeAsFiles(templateDirectory.content)

        try {
            Desktop.getDesktop().open(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    },
    modifier = Modifier
        .size(100.dp, 35.dp)
        .setOnHoverHandCursorEnabled(),
    colors = ButtonDefaults.buttonColors(customGreen)
) {
    Text("Générer", color = Color.White)
}

private fun buildNodeAsFiles(node: Node, sourcePath: String = ""): File {
    val path = "$sourcePath${node.path}"
        .replace("{", "")
        .replace("}", "")
    val file = File(path)
    if (!file.exists()) {
        if (node.isDirectory) {
            file.mkdirs()
        } else {
            file.createNewFile()
        }
    }

    node.children.forEach {
        buildNodeAsFiles(it, "$path\\")
    }

    return file
}
