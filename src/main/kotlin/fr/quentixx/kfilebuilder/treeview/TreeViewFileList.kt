package fr.quentixx.kfilebuilder.treeview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.components.IconArrowDown
import fr.quentixx.kfilebuilder.components.IconArrowRight
import fr.quentixx.kfilebuilder.components.IconDir
import fr.quentixx.kfilebuilder.components.IconFile
import fr.quentixx.kfilebuilder.ext.hasNoSubDirectories
import fr.quentixx.kfilebuilder.data.Node
import java.io.File

@Composable
fun FileList(
    currentSelectedFile: MutableState<File?>,
    dir: File,
    onlyDirectories: Boolean,
    paddingSave: Dp = 0.dp
) {
    println("FileList : Dir : ${dir.path}")
    Column {
        dir.listFiles()?.forEach { file ->
            println("FileRow : Dir : ${file.path}")
            FileRow(currentSelectedFile, file, paddingSave, onlyDirectories)
        }
    }
}

@Composable
fun NodeList(
    mutableNode: MutableState<Node>,
    paddingSave: Dp = 0.dp,
    nodeRowScope: @Composable NodeRowScope
) {
    val node = mutableNode.value

    Column {
        node.children.forEach {
            val mutableChild = remember { mutableStateOf(it) }

            NodeRow(mutableChild, paddingSave, nodeRowScope)

            // Update the current children in the loop with the new children information
            mutableChild.value.apply {
                it.path = path
                it.lastUpdated = lastUpdated
            }
        }
    }
}

@Composable
fun FileRow(
    currentSelectedFile: MutableState<File?>,
    file: File,
    paddingSave: Dp = 0.dp,
    onlyDirectories: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val padding = paddingSave + 16.dp
    val isDir = file.isDirectory
    val isEmptyDir = isDir && file.hasNoSubDirectories()

    val isCurrentlySelected = currentSelectedFile.value == file

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding)
            .clickable {
                currentSelectedFile.value = file

                if (!isEmptyDir) expanded = !expanded
            }
            .background(
                if (isCurrentlySelected) Color.Gray else Color.Transparent
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isDir) {
            var spacingSize = 8.dp

            if (isEmptyDir) {
                spacingSize *= 2
            } else {
                if (expanded) {
                    IconArrowDown(8.dp)
                } else {
                    IconArrowRight(8.dp)
                }
            }

            Spacer(Modifier.width(spacingSize))
            IconDir()
        } else if (!onlyDirectories) {
            IconFile()
        } else {
            return@Row
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = file.name,
            color = Color.White
        )
    }

    if (expanded && file.isDirectory) {
        FileList(currentSelectedFile, file, onlyDirectories, padding)
    }
}

data class NodeRowData(
    val node: MutableState<Node>,
    val hovered: Boolean = false,
)

fun interface NodeRowScope {
    @Composable
    fun consume(data: MutableState<NodeRowData>)
}

@Composable
fun NodeRow(
    mutableNode: MutableState<Node>,
    paddingSave: Dp = 0.dp,
    nodeRowConsumer: @Composable NodeRowScope
) {
    var expanded by remember { mutableStateOf(false) }
    val padding = paddingSave + 16.dp
    val node = mutableNode.value
    val isDir = node.isDirectory
    val isEmptyDir = isDir && node.children.isEmpty()

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState().value

    val data = remember { mutableStateOf(NodeRowData(mutableNode, isHovered)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding)
            .clickable {
                if (!isEmptyDir) expanded = !expanded
            }
            .hoverable(interactionSource),
        verticalAlignment = Alignment.CenterVertically
    ) {

        data.value = data.value.copy(hovered = isHovered)

        RenderNodeElement(mutableNode, expanded, data, nodeRowConsumer)
    }

    if (expanded && node.isDirectory) {
        NodeList(mutableNode, padding, nodeRowConsumer)
    }
}

@Composable
fun RenderNodeElement(
    mutableNode: MutableState<Node>,
    expanded: Boolean,
    data: MutableState<NodeRowData> = mutableStateOf(NodeRowData(mutableNode, false)),
    nodeRowConsumer: @Composable NodeRowScope
) {
    val node = mutableNode.value
    var spacingSize = 8.dp

    if (node.children.isEmpty()) {
        spacingSize *= 2
    }

    if (node.isDirectory) {
        if (node.children.isNotEmpty()) {
            if (expanded) IconArrowDown(8.dp)
            else IconArrowRight(8.dp)
        }
        Spacer(Modifier.width(spacingSize))
        IconDir()
    } else {
        Spacer(Modifier.width(spacingSize))
        IconFile()
    }

    Spacer(Modifier.width(8.dp))
    EditableHighlightedText(mutableNode)

    nodeRowConsumer.consume(data)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditableHighlightedText(mutableNode: MutableState<Node>) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = mutableNode.value.path,
        onValueChange = { newText ->
            mutableNode.value = mutableNode.value.copy(
                path = newText,
                lastUpdated = System.currentTimeMillis()
            )
        },
        keyboardActions = KeyboardActions(onDone = {
            // Cacher le clavier lors de l'appui sur "Entrée"
            keyboardController?.hide()
        }),
        singleLine = true,
        modifier = Modifier
            .onPreviewKeyEvent {
                if (it.key == Key.Enter) {
                    keyboardController?.hide() // Cacher le clavier lorsque "Entrée" est pressée
                    true // Empêcher la propagation de l'événement de clavier
                } else {
                    false // Laisser passer les autres touches
                }
            }
    )
}
