package fr.quentixx.kfilebuilder.treeview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.components.*
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import fr.quentixx.kfilebuilder.data.Node
import java.io.File

private data class NodeRowData(
    val node: MutableState<Node>,
    val hovered: Boolean = false,
)

private fun interface NodeRowScope {
    @Composable
    fun consume(data: MutableState<NodeRowData>)
}

/**
 * Shows a tree view builder.
 */
@Composable
fun TreeViewBuilder(mutableNode: MutableState<Node>) {
    val listState = rememberLazyListState()

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item { SourceNodeLine(mutableNode) }
            item {
                NodeList(mutableNode) {
                    it.value.apply {
                        if (hovered) {
                            if (node.value.isDirectory) {
                                DirectoryNodeControls(node)
                                Spacer(Modifier.width(32.dp))
                            }
                            DeleteNodeButton(it)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Represents first line that shows the parent node of the structure.
 */
@Composable
private fun SourceNodeLine(mutableNode: MutableState<Node>) {
    Spacer(Modifier.height(4.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(16.dp))
        RenderNodeElement(mutableNode, true) {
            Spacer(Modifier.width(32.dp))
            it.value.apply {
                SelectNodeSourceButton(node)
                DirectoryNodeControls(node)
            }
        }
    }
}

@Composable
private fun DirectoryNodeControls(node: MutableState<Node>) {
    val NEW_DIR_PATH = "Nouveau dossier"
    val NEW_FILE_PATH = "Nouveau fichier"

    Spacer(Modifier.width(32.dp))
    TemplateAddDirIcon(
        onClick = {
            val idOfDirectory = node.value.children.count { it.path.contains(NEW_DIR_PATH) }
            val nodeName = NEW_DIR_PATH + if (idOfDirectory > 0) {
                " $idOfDirectory"
            } else ""

            node.value = node.value.copy(
                lastUpdated = System.currentTimeMillis()
            ).apply {
                children.add(
                    Node(nodeName, true)
                )
            }
        }
    )

    Spacer(Modifier.width(32.dp))

    TemplateAddFileIcon(
        onClick = {
            val idOFile = node.value.children.count { it.path.contains(NEW_FILE_PATH) }
            val nodeName = NEW_FILE_PATH + if (idOFile > 0) {
                " $idOFile"
            } else ""

            node.value = node.value.copy(
                lastUpdated = System.currentTimeMillis()
            ).apply {
                children.add(
                    Node( nodeName, false)
                )
            }
        }
    )
}

@Composable
private fun DeleteNodeButton(nodeRow: MutableState<NodeRowData>) {
    IconButton(
        onClick = {
            // TODO: Delete the node from parent children
        },
        modifier = Modifier
            .size(30.dp)
            .setOnHoverHandCursorEnabled()
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Supprimer",
            tint = Color.Red
        )
    }
}

@Composable
private fun SelectNodeSourceButton(node: MutableState<Node>) {
    val isOverlayVisible = remember { mutableStateOf(false) }
    SearchDirectoryIcon(
        onClick = {
            isOverlayVisible.value = true
        },
    )

    if (isOverlayVisible.value) {
        OpenTreeViewSelectorWindow(node, onClose = { isOverlayVisible.value = false })
    }
}


/**
 * This method opens a [ComposableTreeView] and modifies if a directory was returned.
 *
 * @param node The [MutableState] node to open and to change.
 * @param onClose The close request to call when the tree is closed.
 */
@Composable
private fun OpenTreeViewSelectorWindow(
    node: MutableState<Node>,
    onClose: () -> Unit
) {
    val sourceDir = File(node.value.path)
    val start = System.currentTimeMillis()

    TreeViewSelectorWindow(sourceDir, true) {
        onClose.invoke()

        if (it != null) {
            node.value = node.value.copy(path = it.absolutePath)
            println("The returned dir is : ${it.name}")
        }

        val end = System.currentTimeMillis()

        println("End of TreeViewSelector : Execution took : ${(end - start) / 1000}s")

    }
}

@Composable
private fun NodeList(
    mutableNode: MutableState<Node>,
    paddingSave: Dp = 16.dp,
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
private fun NodeRow(
    mutableNode: MutableState<Node>,
    paddingSave: Dp = 0.dp,
    nodeRowConsumer: @Composable NodeRowScope
) {
    var expanded by remember { mutableStateOf(true) }
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
            .height(36.dp)
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
private fun RenderNodeElement(
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

@Composable
private fun EditableHighlightedText(mutableNode: MutableState<Node>) {
    BasicTextField(
        value = mutableNode.value.path,
        onValueChange = { newText ->
            mutableNode.value = mutableNode.value.copy(
                path = newText,
                lastUpdated = System.currentTimeMillis()
            )
        },
        singleLine = true,
    )
}
