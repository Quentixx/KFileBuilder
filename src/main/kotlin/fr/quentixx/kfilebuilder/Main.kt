package fr.quentixx.kfilebuilder

import mu.KotlinLogging
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.system.exitProcess
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import fr.quentixx.kfilebuilder.tabs.ConfigurationTab
import fr.quentixx.kfilebuilder.tabs.InfoTab
import fr.quentixx.kfilebuilder.tabs.templates.TemplatesTab
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled

private val logger = KotlinLogging.logger { }

fun main() = application {
    Window(
        state = rememberWindowState(
            position = WindowPosition(Alignment.Center),
        ),
        title = "KFileBuilder",
        icon = painterResource("icons/FolderIcon.png"),
        onCloseRequest = {
            logger.info { "Ending program" }
            exitProcess(0)
        }) {
        logger.info { "Starting program" }

        App()
    }
}

@Composable
@Preview
fun App() {
    var selectedTab by remember { mutableStateOf(0) }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
            Column {
                TabRow(selectedTabIndex = selectedTab) {
                    val tabs = listOf("Templates", "Configuration", "Info")
                    tabs.forEachIndexed { index, tabName ->
                        Tab(
                            text = { Text(tabName) },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            modifier = Modifier.setOnHoverHandCursorEnabled()
                        )
                    }
                }

                when (selectedTab) {
                    0 -> TemplatesTab()
                    1 -> ConfigurationTab()
                    2 -> InfoTab()
                }
            }
        }
    }
}
