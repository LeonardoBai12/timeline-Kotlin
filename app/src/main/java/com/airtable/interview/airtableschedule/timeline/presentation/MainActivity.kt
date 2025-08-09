package com.airtable.interview.airtableschedule.timeline.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TimelineRoute()
        }
    }

    @Composable
    private fun TimelineRoute(
        viewModel: TimelineViewModel = hiltViewModel()
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle()

        TimelineScreen(
            state = state,
            eventFlow = viewModel.uiEvent,
            onEvent = viewModel::onEvent
        )
    }
}
