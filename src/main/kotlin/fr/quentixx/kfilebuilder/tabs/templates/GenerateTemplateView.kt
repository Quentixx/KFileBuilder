package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.components.GenericConfirmButton
import fr.quentixx.kfilebuilder.components.GenericConfirmWindow
import fr.quentixx.kfilebuilder.components.GenericUnconfirmButton
import fr.quentixx.kfilebuilder.json.Node
import fr.quentixx.kfilebuilder.json.TemplateDirectory
import fr.quentixx.kfilebuilder.tabs.templates.commons.TemplateScreen
import fr.quentixx.kfilebuilder.tabs.templates.commons.TemplateScreenManager
import java.awt.Desktop
import java.io.File
import java.io.IOException

data class GenerationData(
    val replacements: MutableMap<String, String>,
    val lastUpdated: Long = System.currentTimeMillis(),
)

/**
 * Represents the view of template generation confirmation.
 * If some parameters are recognized in files names, the view will ask to enter
 * the values to display in the template.
 * @param screenManager To navigate through the different views.
 */
@Composable
fun GenerateTemplateView(screenManager: TemplateScreenManager) {
    val template = screenManager.selectedTemplate!!
    val args = findParamsInStructure(template.content)
    val listState = rememberLazyListState()

    val mutableGenerationData = remember { mutableStateOf(GenerationData(mutableMapOf())) }
    val generationData = mutableGenerationData.value
    val confirmGenerationOverlay = remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxHeight()
            .background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(64.dp))

        Row {
            Text(
                "Générer le modèle '${template.name}'\ndans le dossier '${template.content.path}'",
                fontSize = TextUnit(1F, TextUnitType.Em),
                maxLines = 2,
                style = TextStyle(
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(Modifier.height(64.dp))

        if (args.isNotEmpty())
            GenerationArgumentsList(args, mutableGenerationData, listState)

        Spacer(Modifier.height(64.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            val s = generationData.replacements.size
            val ss = args.size

            GenericUnconfirmButton("Retour") {
                screenManager.navigateTo(TemplateScreen.MAIN_VIEW)
            }

            if (generationData.replacements.size == args.size) { // Replacements are completed
                Spacer(Modifier.width(32.dp))

                GenericConfirmButton("Suivant") {
                    confirmGenerationOverlay.value = true
                }
            }
        }

    }

    Spacer(Modifier.height(64.dp))

    if (confirmGenerationOverlay.value) {
        ConfirmGenerationWindow(screenManager, template, generationData.replacements)
    }
}

@Composable
private fun GenerationArgumentsList(
    args: List<String>,
    mutableGenerationData: MutableState<GenerationData>,
    listState: LazyListState
) {
    val generationData = mutableGenerationData.value

    LazyColumn(
        state = listState,
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        itemsIndexed(args) { index, value ->
            TextField(
                value = generationData.replacements[value] ?: value,
                modifier = Modifier
                    .background(Color.DarkGray)
                    .requiredWidth(200.dp),
                onValueChange = {
                    mutableGenerationData.value = generationData.copy(
                        replacements = generationData.replacements.apply {
                            put(args[index], it)
                        },
                        lastUpdated = System.currentTimeMillis()
                    )

                },
                label = { Text("Remplacer paramètre") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                )
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ConfirmGenerationWindow(
    screenManager: TemplateScreenManager,
    template: TemplateDirectory,
    replacements: Map<String, String>
) {

    GenericConfirmWindow(
        confirmText = "Confirmez la génération du modèle",
        onBack = { screenManager.navigateTo(TemplateScreen.MAIN_VIEW) },
        onConfirm = {
            screenManager.navigateTo(TemplateScreen.MAIN_VIEW)

            val file = buildNodeAsFiles(template.content, replacements)
            try {
                Desktop.getDesktop().open(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    )
}

private fun findParamsInStructure(node: Node): List<String> {
    val listOfParams = mutableListOf<String>()
    val addedParams = HashSet<String>()

    fun processNode(currentNode: Node) {
        val paramsInPath = findParamsInPath(currentNode.path)
        for (param in paramsInPath) {
            if (param !in addedParams) {
                listOfParams.add(param)
                addedParams.add(param)
            }
        }

        for (child in currentNode.children) {
            processNode(child)
        }
    }

    processNode(node)
    return listOfParams
}


private fun findParamsInPath(path: String): Set<String> {
    val setOfParams = mutableSetOf<String>()
    var insideBraces = false
    var currentParam = ""

    for (char in path) {
        if (char == '{') {
            insideBraces = true
        } else if (char == '}') {
            insideBraces = false
            if (currentParam.isNotEmpty()) {
                setOfParams.add(currentParam)
                currentParam = ""
            }
        } else {
            if (insideBraces) {
                currentParam += char
            }
        }
    }

    return setOfParams
}

private fun buildNodeAsFiles(
    node: Node,
    replacements: Map<String, String>,
    sourcePath: String = ""
): File {
    val pathWithReplacements = node.path.replace(Regex("\\{([^}]*)\\}")) {
        val argument = it.groupValues[1]
        replacements[argument] ?: it.value
    }

    val file = File("$sourcePath$pathWithReplacements")

    if (!file.exists()) {
        if (node.isDirectory) {
            file.mkdirs()
        } else {
            file.createNewFile()
        }
    }

    node.children.forEach {
        buildNodeAsFiles(it, replacements, "$sourcePath$pathWithReplacements\\")
    }

    return file
}