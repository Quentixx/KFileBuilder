package fr.quentixx.kfilebuilder.treeview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.components.TemplateAddDirIcon
import fr.quentixx.kfilebuilder.components.TemplateAddFileIcon
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import fr.quentixx.kfilebuilder.json.Node
import java.io.File

/**
 * Shows a tree view builder.
 */
@Composable
fun TreeViewBuilder(mutableNode: MutableState<Node>){
    val node = mutableNode.value
    val listState = rememberLazyListState()

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item { SourceNodeLine(mutableNode) }
            item {
                NodeList(mutableNode) {
                    if (it.value.isDirectory)
                        DirectoryNodeControls(it)
                }
            }


        }

        // println("Box updated: $node")
    }
}

@Composable
private fun SourceNodeLine(mutableNode: MutableState<Node>) {
    Row {
        RenderNodeElementRow(mutableNode, true) {
            Spacer(Modifier.width(32.dp))
            SelectNodeSourceButton(it)
            DirectoryNodeControls(it)
        }
    }
}

@Composable
fun DirectoryNodeControls(mutableNode: MutableState<Node>) {
    Spacer(Modifier.width(32.dp))

    TemplateAddDirIcon(
        onClick = {
            mutableNode.value = mutableNode.value.copy(
                lastUpdated = System.currentTimeMillis()
            ).apply {
                children.add(
                    Node("New dir", true)
                )
            }
        }
    )

    Spacer(Modifier.width(32.dp))

    TemplateAddFileIcon(
        onClick = {
            mutableNode.value = mutableNode.value.copy(
                lastUpdated = System.currentTimeMillis()
            ).apply {
                children.add(
                    Node("New file", false)
                )
            }
        }
    )
}

@Composable
private fun SelectNodeSourceButton(node: MutableState<Node>) {
    val isOverlayVisible = remember { mutableStateOf(false) }
    Button(
        onClick = {
            isOverlayVisible.value = true
        },
        modifier = Modifier
            .size(80.dp, 32.dp)
            .setOnHoverHandCursorEnabled()
    ) {
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
