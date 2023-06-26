package ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import java.awt.Cursor

/**
 * Extension function to simplify usage of [pointerHoverIcon] in the case when the current element is hovered.
 * When this function is called, the cursor of the mouse is set to [Cursor.HAND_CURSOR].
 */
fun Modifier.setOnHoverHandCursorEnabled() =
    pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))