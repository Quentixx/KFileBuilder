package fr.quentixx.kfilebuilder.components

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
import fr.quentixx.kfilebuilder.ext.setOnHoverHandCursorEnabled

/**
 * Represents a generic window of confirmation.
 * @param confirmText The confirmation message to display.
 * @param onBack The function to call when confirmation is ended.
 * @param onConfirm The function to call when the process is confirmed.
 */
@Composable
fun GenericConfirmWindow(
    confirmText: String = "Confirmation process",
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    additionalContent: @Composable RowScope.() -> Unit = {}
): Unit = Window(
    title = "Confirmation",
    onCloseRequest = {
        onBack.invoke()
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

        Row {
            additionalContent.invoke(this)
        }

        Text(confirmText)
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GenericConfirmButton {
                onConfirm.invoke()
                onBack.invoke()
            }
            Spacer(Modifier.width(32.dp))
            GenericUnconfirmButton { onBack.invoke() }
        }
    }
}

/**
 * Generic customizable button that's represents a confirm button.
 * The color of this button is [customGreen].
 * @param buttonText The text to display on the button.
 * @param clickEvent The function called to handle action after click.
 */
@Composable
fun GenericConfirmButton(
    buttonText: String = "Confirm",
    clickEvent: () -> Unit
) {
    Button(
        onClick = {
            clickEvent.invoke()
        },
        modifier = Modifier.setOnHoverHandCursorEnabled(),
        colors = ButtonDefaults.buttonColors(customGreen)
    ) {
        Text(buttonText)
    }
}

/**
 * Generic customizable button that's represents an unconfirmed button.
 * The color of this button is [Color.Red].
 * @param buttonText The text to display on the button.
 * @param clickEvent The function called to handle action after click.
 */
@Composable
fun GenericUnconfirmButton(
    buttonText: String = "Cancel",
    clickEvent: () -> Unit
) {
    Button(
        onClick = {
            clickEvent.invoke()
        },
        modifier = Modifier.setOnHoverHandCursorEnabled(),
        colors = ButtonDefaults.buttonColors(Color.Red)
    ) {
        Text(buttonText)
    }
}
