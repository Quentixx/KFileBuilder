package fr.quentixx.kfilebuilder.tabs.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import fr.quentixx.kfilebuilder.color.customGreen
import fr.quentixx.kfilebuilder.json.TemplatesService

@Composable
fun DeleteTemplateView(screenManager: TemplateScreenManager) {
    Window(
        title = "Confirmation",
        onCloseRequest = {
            screenManager.navigateTo(TemplateScreen.MAIN_VIEW)
        },
        resizable = false,
        focusable = false,
        alwaysOnTop = true,
        state = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(500.dp, 300.dp)
        )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Gray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Êtes-vous sûr de vouloir supprimer ce modèle ?")
            Spacer(Modifier.height(64.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ConfirmDeleteTemplateButton(screenManager)

                Spacer(Modifier.width(32.dp))

                UnconfirmDeleteTemplateButton(screenManager)
            }
        }
    }
}

@Composable
private fun ConfirmDeleteTemplateButton(
    screenManager: TemplateScreenManager
) {
    Button(
        onClick = {
            TemplatesService.delete(screenManager.selectedTemplate!!)
            screenManager.navigateTo(TemplateScreen.MAIN_VIEW)
        },
        colors = ButtonDefaults.buttonColors(customGreen)
    ) {
        Text("Oui")
    }
}

@Composable
private fun UnconfirmDeleteTemplateButton(
    screenManager: TemplateScreenManager
) {
    Button(
        onClick = {
            screenManager.navigateTo(TemplateScreen.MAIN_VIEW)
        },
        colors = ButtonDefaults.buttonColors(Color.Red)
    ) {
        Text("Non")
    }
}
