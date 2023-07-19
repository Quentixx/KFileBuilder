package fr.quentixx.kfilebuilder.tabs.templates.create

import fr.quentixx.kfilebuilder.components.TemplateAddDirIcon
import fr.quentixx.kfilebuilder.components.TemplateAddFileIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import fr.quentixx.kfilebuilder.treeview.TreeViewSelectorWindow
import java.io.File

/**
 * This method shows the current template as editable tree view.
 */
@Composable
fun TemplateBuilderView() {
    println("TemplateBuilderView")
    val rootNode = mutableStateOf(Node(System.getProperty("user.home"), true))

    NodeView(rootNode)
}

@Composable
fun NodeView(node: MutableState<Node>) {
    Column(
        Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            Modifier.background(Color.Blue)
        ) {
            NodeEditRow(node)
        }

        val listOfChildren = node.value.children.sortedByDescending { it.isFolder }
        for (childNode in listOfChildren) {
            println("\nChildNode Info: $childNode")

            val childState = remember { mutableStateOf(childNode) }
            NodeView(childState)
        }

    }
}

@Composable
fun NodeEditRow(node: MutableState<Node>) {
    // A row with the Node properties and the controls to manipulate it
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        RootNodePathText(node)

        Spacer(Modifier.width(32.dp))

        if (node.value.isFolder) {
            // Spacer(Modifier.width(16.dp))
            TemplateAddDirIcon(
                onClick = {
                    val parentNode = node.value
                    val newNode = Node("New directory", true, parentNode)
                    val children = node.value.children + newNode

                    println("Click add dir on node: ${node.value.name}")

                    node.value = node.value.copy(
                        children = children
                    )
                }
            )

            Spacer(Modifier.width(32.dp))

            TemplateAddFileIcon(
                onClick = {
                    node.value = node.value.copy(
                        children = node.value.children + Node("New file", false)
                    )
                    //node.value = node.value.copy(children = node.value.children + Node("New File", false))
                }
            )

            Spacer(Modifier.width(32.dp))

            OpenTreeViewSelectorButton(node)
        }
    }
}

@Composable
fun RootNodePathText(node: MutableState<Node>) =
    TextField(
        value = node.value.name,
        onValueChange = {
            val formattedValue = it.replace(" ", "")

            node.value = node.value.copy(name = formattedValue)
            println("The node path is : $formattedValue")
        },
        modifier = Modifier.size(300.dp, 50.dp),
    )

@Composable
fun RemoveButton(node: MutableState<Node>) = IconButton(
    onClick = {

    },
    modifier = Modifier
        .size(25.dp)
        .setOnHoverHandCursorEnabled()
) {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = "Supprimer",
        tint = Color.Red
    )
}

@Composable
fun OpenTreeViewSelectorButton(node: MutableState<Node>) {
    val isOverlayVisible = remember { mutableStateOf(false) }
    Button(onClick = {
        isOverlayVisible.value = true
    }) {
        Text(text = "Select")
    }

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
    val sourceDir = File(node.value.name)
    val start = System.currentTimeMillis()

    TreeViewSelectorWindow(sourceDir, true) {
        onClose.invoke()

        if (it != null) {
            node.value = node.value.copy(name = it.absolutePath)
            println("The returned dir is : ${it.name}")
        }

        val end = System.currentTimeMillis()

        println("End of TreeViewSelector : Execution took : ${(end - start) / 1000}s")

    }
}