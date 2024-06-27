package com.example.loop_new.presentation.screens.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loop_new.domain.model.firebase.Favorite

@Composable
fun DeleteFavoriteStoryDialog(
    story: Favorite,
    onDelete: () -> Unit,
    onClose: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Do you want to delete this story:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = story.title ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    onDelete()
                },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(bottom = 4.dp),
                onClick = {
                    onClose()
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}
