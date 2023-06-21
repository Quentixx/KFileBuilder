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
import tab.ConfigurationTab
import tab.InfoTab
import tab.TemplatesTab
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val logger = KotlinLogging.logger { }

@Composable
@Preview
fun App() {
    var selectedTab by remember { mutableStateOf(0) }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
            Column {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        text = { Text("Configuration") },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    Tab(
                        text = { Text("Templates") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    Tab(
                        text = { Text("Info") },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                }

                when (selectedTab) {
                    0 -> ConfigurationTab()
                    1 -> TemplatesTab()
                    2 -> InfoTab()
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = {
        logger.info { "Ending program" }
        exitProcess(0)
    }) {
        logger.info { "Starting program" }
        App()
    }
}