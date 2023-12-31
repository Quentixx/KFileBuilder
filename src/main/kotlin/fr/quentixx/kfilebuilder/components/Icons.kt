package fr.quentixx.kfilebuilder.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled

@Composable
fun IconFile() = Icon(
    painterResource("icons/FileIcon.png"),
    null, Modifier.size(20.dp)
)

@Composable
fun IconDir() = Icon(
    painterResource("icons/FolderIcon.png"),
    null, Modifier.size(20.dp),
    Color.Yellow
)

@Composable
fun TemplateAddDirIcon(
    onClick: () -> Unit,
    size: Dp = 20.dp
) = Icon(
    painterResource("icons/Template_AddDirectory.png"),
    null, Modifier
        .size(size)
        .clickable { onClick.invoke() }
        .setOnHoverHandCursorEnabled(),
    Color.Black
)

@Composable
fun TemplateAddFileIcon(
    onClick: () -> Unit,
    size: Dp = 20.dp
) = Icon(
    painterResource("icons/Template_AddFile.png"),
    null, Modifier
        .size(size)
        .clickable { onClick.invoke() }
        .setOnHoverHandCursorEnabled(),
    Color.Black
)

@Composable
fun IconArrowDown(
    size: Dp = 10.dp
) = Icon(
    painterResource("icons/TreeArrow_Down.png"),
    null, Modifier.size(size), Color.LightGray
)

@Composable
fun IconArrowRight(
    size: Dp = 10.dp
) = Icon(
    painterResource("icons/TreeArrow_Right.png"),
    null, Modifier.size(size), Color.LightGray
)

@Composable
fun IconBackParentDir(
    onClick: () -> Unit,
    size: Dp = 10.dp
) = Icon(
    painterResource("icons/TreeView_BackParentDir.png"),
    null, Modifier
        .size(size)
        .clickable { onClick.invoke() }
        .setOnHoverHandCursorEnabled(),
    Color.White
)

@Composable
fun IconBackGoParentDir(
    onClick: () -> Unit,
    size: Dp = 10.dp
) = Icon(
    painterResource("icons/TreeView_GoParentDir.png"),
    null, Modifier
        .size(size)
        .clickable { onClick.invoke() }
        .setOnHoverHandCursorEnabled(),
    Color.White
)

