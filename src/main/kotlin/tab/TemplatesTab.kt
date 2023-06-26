package tab

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.style.TextOverflow
import ext.setOnHoverHandCursorEnabled

@Composable
fun TemplatesTab() {
    val templates = listOf(
        "Template of satoshi",
        "Template czc2",
        "Template fzfzf",
        "Template cccccc",
        "Template xxxxxx",
        "Template zxzsxs",
        "Template eaeaeaeaea",
        "Template sxssxsxsxsx",
        "Template 1121245",
        "Template 1",
        "Template 2",
        "Template 3"
    )

    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))

            TopMenu()

            Spacer(modifier = Modifier.height(16.dp))

            val listState = rememberLazyListState()

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(templates) { template ->
                        TemplateLine(template) // Show the template line with [name, edit button, delete button]
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // Adding Vertical Scrollbar
                TemplatesScrollbar(listState)
            }
        }

        Box(Modifier.fillMaxSize().background(Color.Gray)) {
            // template details
        }
    }
}

@Composable
fun TopMenu() = Row(
    Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
) {
    Button(
        onClick = {
            // TODO: Create Template redirection
        },
        modifier = Modifier.setOnHoverHandCursorEnabled()
    ) {
        Text("Créer une template", color = Color.White)
    }
}

@Composable
fun TemplatesScrollbar(listState: LazyListState) {
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


@Composable
fun TemplateLine(template: String) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = template,
                color = Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(16.dp)) // Add horizontal spacing between text and buttons

        Row(modifier = Modifier.weight(1f)) {
            EditTemplateIcon()
            DeleteTemplateIcon()
        }
    }
}


@Composable
fun EditTemplateIcon() = Button(
    onClick = {
        // TODO: Edit template redirection
    },
    modifier = Modifier
        .size(100.dp, 35.dp)
        .setOnHoverHandCursorEnabled()
) {
    Text("Editer", color = Color.White)
}

@Composable
fun DeleteTemplateIcon() =
    IconButton(
        onClick = {
            // TODO: Delete template confirmation redirection
        },
        modifier = Modifier
            .size(50.dp)
            .setOnHoverHandCursorEnabled()
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Supprimer",
            tint = Color.Red
        )
    }