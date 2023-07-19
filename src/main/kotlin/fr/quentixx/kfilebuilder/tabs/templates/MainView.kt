package fr.quentixx.kfilebuilder.tabs.templates

import fr.quentixx.kfilebuilder.components.Scrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled
import fr.quentixx.kfilebuilder.json.TemplateDirectory

/**
 * The main view of templates. All templates are listed here, with the name of the template and buttons to edit or delete it.
 */
@Composable
fun MainView(
    templates: List<TemplateDirectory>,
    screenManager: TemplateScreenManager
) {
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))

            TopMenu(screenManager)

            Spacer(modifier = Modifier.height(16.dp))

            val listState = rememberLazyListState()

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(templates) { template ->
                        TemplateLine(template.name) // Show the template line with [name, edit button, delete button]
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // Adding Vertical Scrollbar
                Scrollbar(listState)
            }
        }

        Box(Modifier.fillMaxSize().background(Color.Gray)) {
            // template details
        }
    }
}

@Composable
fun TopMenu(screenManager: TemplateScreenManager) = Row(
    Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
) {
    Button(
        onClick = {
            screenManager.navigateTo(TemplateScreen.CREATE_TEMPLATE_VIEW)
        },
        modifier = Modifier.setOnHoverHandCursorEnabled()
    ) {
        Text("Créer une template", color = Color.White)
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