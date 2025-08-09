package io.lb.schedule.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@ExperimentalMaterial3Api
@Composable
fun EventAppBar(
    title: String? = null,
    onNavigationIconClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            title?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = it,
                    fontSize = 22.sp
                )
            }
        },
        navigationIcon = {
            onNavigationIconClick?.let {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Toggle drawer"
                    )
                }
            }
        }
    )
}
