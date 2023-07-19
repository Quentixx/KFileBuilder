package fr.quentixx.kfilebuilder.treeview

import fr.quentixx.kfilebuilder.components.IconArrowDown
import fr.quentixx.kfilebuilder.components.IconArrowRight
import fr.quentixx.kfilebuilder.components.IconBackGoParentDir
import fr.quentixx.kfilebuilder.components.IconBackParentDir
import fr.quentixx.kfilebuilder.components.IconDir
import fr.quentixx.kfilebuilder.components.IconFile
import fr.quentixx.kfilebuilder.components.Scrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import fr.quentixx.kfilebuilder.ext.hasNoSubDirectories
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import java.io.File
import javax.swing.JFileChooser

/**
 * Functional interface to handle the return [File] after the tree view selection.
 */
fun interface TreeViewSelectorConsumer {
    fun onClose(selectedFile: File?)
}

/**
 * This method opens a new window containing a tree view allowing to explore and select a file.
 *
 * @param sourceDir The source directory to analyse in the tree view.
 * @param onlyDirectories If true, only directories will be shown. False by default.
 * @param selectorConsumer Represents the consumer to call when the window is closed or when a file is selected.
 */
@Composable
fun TreeViewSelectorWindow(
    sourceDir: File,
    onlyDirectories: Boolean = false,
    selectorConsumer: TreeViewSelectorConsumer
) {

    Window(
        title = "Select a folder",
        onCloseRequest = { selectorConsumer.onClose(null) },
        resizable = false,
        focusable = false,
        alwaysOnTop = true,
        state = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(500.dp, 300.dp)
        )
    ) {

        TreeViewSelectorBox(
            sourceDir,
            onlyDirectories,
            selectorConsumer
        )
    }
}

/**
 * This method creates a box containing a tree view allowing to explore and select a file.
 *
 * @param sourceDir The source directory to analyse in the tree view.
 * @param onlyDirectories If true, only directories will be shown. False by default.
 * @param selectorConsumer Represents the consumer to call when a file is selected.
 */
@Composable
fun TreeViewSelectorBox(
    sourceDir: File,
    onlyDirectories: Boolean,
    selectorConsumer: TreeViewSelectorConsumer
) {

    val previousMutableDir = remember { mutableStateOf<File?>(null) }
    val currentMutableDir = remember { mutableStateOf(sourceDir) }
    val stateConfirmed = remember { mutableStateOf(false) }
    val currentSelectedFile = remember { mutableStateOf<File?>(null) }

    if (stateConfirmed.value) {
        selectorConsumer.onClose(currentSelectedFile.value)
        return
    }

    val listState = rememberLazyListState()
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                TreeViewTopItem(
                    sourceDir, previousMutableDir, currentMutableDir,
                    currentSelectedFile, stateConfirmed
                )
            }

            // List current directory children
            currentMutableDir.value.listFiles()?.let {
                items(it) { file ->
                    FileRow(currentSelectedFile, file, 0.dp, onlyDirectories)
                }
            }
        }
    }

    Scrollbar(listState)

    println("Current selected dir : ${currentMutableDir.value.name}")
}

/**
 * This method handles the first row on top with Go Back Button,
 */
@Composable
private fun TreeViewTopItem(
    sourceDir: File,
    previousMutableDir: MutableState<File?>,
    currentMutableDir: MutableState<File>,
    currentSelectedFile: MutableState<File?>,
    stateConfirmed: MutableState<Boolean>
) {
    val dir = currentMutableDir.value
    Row(
        Modifier.fillMaxWidth()
    ) {
        IconDir()
        Spacer(Modifier.width(8.dp))
        Text(dir.path)

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            // Go to parent directory
            val parentFile = dir.parentFile
            if (parentFile != null) {
                IconBackGoParentDir(
                    onClick = {
                        previousMutableDir.value = dir
                        currentMutableDir.value = parentFile
                    },
                    size = 16.dp
                )
            }
            Spacer(Modifier.width(64.dp))

            // Go to previous directory
            if (previousMutableDir.value != null
                && dir != sourceDir
            ) {
                val previousDir = previousMutableDir.value!!

                IconBackParentDir(
                    onClick = {
                        currentMutableDir.value = previousDir
                        previousMutableDir.value = sourceDir
                    },
                    size = 16.dp
                )
            }


            // Confirm the current selected file
            val selectedFile = currentSelectedFile.value
            if (selectedFile != null) {
                Spacer(Modifier.width(64.dp))
                Text("OK", modifier = Modifier.clickable {
                    stateConfirmed.value = true
                }.setOnHoverHandCursorEnabled())
            }
        }
    }
}


@Composable
private fun ChooseDirectory(onDirectorySelected: (File) -> Unit) {
    val directoryDialogOpen = remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { directoryDialogOpen.value = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Choose Directory")
        }

        if (directoryDialogOpen.value) {
            DirectoryDialog(onDirectorySelected = onDirectorySelected)
        }
    }
}

@Composable
private fun DirectoryDialog(onDirectorySelected: (File) -> Unit) {
    val directory = remember { mutableStateOf(File("").absoluteFile) }

    Column {
        Text("Select a Directory")

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val fileChooser = JFileChooser()
                fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

                val result = fileChooser.showOpenDialog(null)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val selectedDir = fileChooser.selectedFile
                    onDirectorySelected(selectedDir)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Choose")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onDirectorySelected(directory.value) },
            enabled = directory.value.isDirectory,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Open")
        }
    }
}

@Composable
private fun FileList(
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
private fun FileRow(
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