package fr.quentixx.kfilebuilder.components

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled

/**
 * Represents a custom scroll bar UI component.
 *
 * @param listState The state of the list for which the scrollbar will be used.
 */
@Composable
fun Scrollbar(listState: LazyListState) {
    val scrollbarStyle = ScrollbarStyle(
        minimalHeight = 16.dp,
        thickness = 8.dp,
        shape = RoundedCornerShape(4.dp),
        hoverDurationMillis = 300,
        unhoverColor = Color.White.copy(alpha = 0.5f),
        hoverColor = Color.Black.copy(alpha = 0.50f)
    )

    Box(Modifier.fillMaxSize()) {
        VerticalScrollbar(
            rememberScrollbarAdapter(listState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .setOnHoverHandCursorEnabled(),
            style = scrollbarStyle
        )
    }
}