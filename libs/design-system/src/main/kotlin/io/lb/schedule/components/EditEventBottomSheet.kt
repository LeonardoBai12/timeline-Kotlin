package io.lb.schedule.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lb.schedule.model.Event
import io.lb.schedule.util.createDatePickerDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EventEditBottomSheet(
    event: Event,
    onDismiss: () -> Unit,
    onSaveEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val eventName = remember { mutableStateOf(event.name) }
    val startDateString = remember { mutableStateOf(formatDateToString(event.startDate)) }
    val endDateString = remember { mutableStateOf(formatDateToString(event.endDate)) }
    val startDatePicker = remember {
        context.createDatePickerDialog(startDateString, isDarkTheme = false)
    }
    val endDatePicker = remember {
        context.createDatePickerDialog(endDateString, isDarkTheme = false)
    }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Event",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            DefaultFilledTextField(
                text = eventName,
                label = "Event Name",
                keyboardType = KeyboardType.Text,
                hasCloseButton = true,
                modifier = Modifier.fillMaxWidth()
            )

            DefaultFilledTextField(
                text = startDateString,
                label = "Start Date",
                isEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        Modifier.clickableWithoutRipple {
                            startDatePicker.show()
                        }
                    )
            )

            DefaultFilledTextField(
                text = endDateString,
                label = "End Date",
                isEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        Modifier.clickableWithoutRipple {
                            endDatePicker.show()
                        }
                    )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DefaultTextButton(
                    text = "Cancel",
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = onDismiss
                )

                DefaultTextButton(
                    text = "Save",
                    modifier = Modifier.weight(1f),
                    enabled = eventName.value.isNotBlank(),
                    onClick = {
                        val updatedEvent = event.copy(
                            name = eventName.value.trim(),
                            startDate = parseStringToDate(startDateString.value),
                            endDate = parseStringToDate(endDateString.value)
                        )
                        onSaveEvent(updatedEvent)
                        onDismiss()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun formatDateToString(date: Date): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date)
}

private fun parseStringToDate(dateString: String): Date {
    return try {
        SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(dateString) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}

@Composable
private fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier {
    return this.then(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    )
}